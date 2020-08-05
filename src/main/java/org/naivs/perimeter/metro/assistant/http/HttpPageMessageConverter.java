package org.naivs.perimeter.metro.assistant.http;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.data.entity.ProductProbeEntity;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HttpPageMessageConverter extends AbstractHttpMessageConverter<ProductEntity> {

    public HttpPageMessageConverter() {
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_PLAIN);
        mediaTypes.add(MediaType.APPLICATION_XML);
        mediaTypes.add(MediaType.TEXT_HTML);
        this.setSupportedMediaTypes(mediaTypes);
    }

    @Override
    protected boolean supports(@NotNull Class<?> aClass) {
        return aClass.isAssignableFrom(ProductEntity.class);
    }

    @NotNull
    @Override
    protected ProductEntity readInternal(@NotNull Class<? extends ProductEntity> aClass,
                                         @NotNull HttpInputMessage httpInputMessage
    ) throws IOException, HttpMessageNotReadableException {
        ProductEntity product = new ProductEntity();

        Document doc = Jsoup.parse(httpInputMessage.getBody(), null, "base");
        Elements productElements = doc.getElementsByAttributeValue("itemtype", "http://schema.org/Product");

        parseProductTop(
                product,
                productElements.first().getElementsByClass("b-product-main__top").first()
        );

        parseSidebar(
                product,
                productElements.first().getElementsByClass("b-product-sidebar").first()
        );

        return product;
    }

    @Override
    protected void writeInternal(@NotNull ProductEntity item,
                                 @NotNull HttpOutputMessage httpOutputMessage
    ) throws HttpMessageNotWritableException {
        throw new RuntimeException("Not implemented.");
    }

    private void parseProductTop(ProductEntity product, Element element) {
        // identifier and title
        String name = element.getElementsByAttributeValue("itemprop", "name").text();
        Long id = Long.parseLong(
                element.getElementsByAttributeValue("itemprop", "productID").text()
        );
        product.setName(name);
        product.setId(id);
    }

    private void parseSidebar(ProductEntity product, Element element) {
        // me, regularPrice, wholesalePrice, leftPct
        /*
            data-article="113309"
            data-box_size="1"
            data-count_lvl_1="3.00"
            data-discount_lvl_1="5.00"
            data-count_lvl_2="12.00"
            data-discount_lvl_2="27.17"
            data-is_action_price="0"
            data-opt_cnt="0"
            data-discount="0"
            data-every_x="0"
            data-xby="0"
            data-yby="0"
            data-act_price="0.00"
            data-regular_price="90.49"
            data-old_price="0"
            data-isStockEmpty="0"
         */
        Element priceElement = element.getElementsByClass("b-product-sidebar__cnt").first();
        String me = priceElement.getElementsByClass("b-product-sidebar-price-info").first().text();
        float regularPrice = Float.parseFloat(priceElement.getElementsByClass("int").first().text());
        regularPrice += Float.parseFloat(priceElement.getElementsByClass("float").first().text()) * 0.01f;

        ProductProbeEntity probe = new ProductProbeEntity();
        probe.setProduct(product);
        probe.setRegularPrice(regularPrice);

        // parse opt prices
        Elements optPrices = priceElement.getElementsByClass("opt-price-lvl__item");
        for (Element optPrice : optPrices) {
            String rawAmount = optPrice.getElementsByClass("opt-price-lvl__details").first()
                    .getElementsByTag("b").first().text();
            int amount = Integer.parseInt(rawAmount.substring(0, rawAmount.length() - 3));

            float optPr = Float.parseFloat(optPrice.getElementsByClass("int").text().split(",")[0]);
            optPr += Float.parseFloat(optPrice.getElementsByClass("float").text()) * 0.01f;
            probe.getWholesalePrice().put(amount, optPr);
        }

        Element stock = element.getElementsByClass("b-product-sidebar-stock").first();
        String left = stock.getElementsByClass("b-product-stock_scale").first()
                .getElementsByAttribute("style").first().attr("style");
//        Attributes attributes = stock.getElementsByAttribute("add-to-list").first().attributes();
        probe.setLeftPct(Integer.parseInt(left.substring(6, left.length() - 1)));

        product.setPack(me);
        product.getProbes().add(probe);
    }
}
