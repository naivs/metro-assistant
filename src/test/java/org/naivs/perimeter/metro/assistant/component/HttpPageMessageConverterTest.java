package org.naivs.perimeter.metro.assistant.component;

import org.junit.jupiter.api.Test;
import org.naivs.perimeter.metro.assistant.data.entity.ProductEntity;
import org.naivs.perimeter.metro.assistant.http.HttpPageMessageConverter;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;

class HttpPageMessageConverterTest {

    private HttpPageMessageConverter converter = new HttpPageMessageConverter();

//    private static final ProductEntity[] TEST_PRODUCTS = new ProductEntity[3];

    @Test
    void supports() {
        assertTrue(converter.canRead(ProductEntity.class, MediaType.TEXT_HTML));
        assertTrue(converter.canRead(ProductEntity.class, MediaType.APPLICATION_XML));
        assertTrue(converter.canRead(ProductEntity.class, MediaType.TEXT_PLAIN));
    }

    // TODO: outdated version of html parser. Fix this test when parser will be updated
//    @Test
//    void readInternalFirst() {
//        readTest(0);
//    }
//
//    @Test
//    void readInternalSecond() {
//        readTest(1);
//    }
//
//    private void readTest(int i) {
//        try {
//            byte[] bytes = Files.readAllBytes(
//                    Paths.get("target/classes/static")
//                            .resolve(TEST_PRODUCTS[i].getUrl()));
//            HttpInputMessage inputMessage = new MockHttpInputMessage(bytes);
//
//            MetroProduct metroProduct = converter.readInternal(
//                    MetroProduct.class,
//                    inputMessage
//            );
//
//            assertNotNull(metroProduct);
//            assertEquals(TEST_PRODUCTS[i].getId(), metroProduct.getId());
//            assertEquals(TEST_PRODUCTS[i].getName(), metroProduct.getName());
//            assertEquals(TEST_PRODUCTS[i].getRegularPrice(), metroProduct.getRegularPrice());
////            assertEquals(TEST_PRODUCTS[i].getUrl(), metroProduct.getUrl());
//            assertEquals(TEST_PRODUCTS[i].getLeftPct(), metroProduct.getLeftPct());
//            assertEquals(TEST_PRODUCTS[i].getMe(), metroProduct.getMe());
//            assertEquals(TEST_PRODUCTS[i].getWholesalePrice(), metroProduct.getWholesalePrice());
//        } catch (IOException e) {
//            fail(e);
//        }
//    }

    @Test
    void writeInternal() {
    }
}