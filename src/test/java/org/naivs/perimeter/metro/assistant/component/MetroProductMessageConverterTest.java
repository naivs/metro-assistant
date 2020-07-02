package org.naivs.perimeter.metro.assistant.component;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.naivs.perimeter.metro.assistant.data.model.MetroProduct;
import org.springframework.http.HttpInputMessage;
import org.springframework.mock.http.MockHttpInputMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MetroProductMessageConverterTest {

    private MetroProductMessageConverter converter = new MetroProductMessageConverter();

    private static final MetroProduct[] TEST_PRODUCTS = new MetroProduct[2];

    @BeforeAll
    static void setUp() {
        TEST_PRODUCTS[0] = new MetroProduct(
                113309L,
                "Молоко PARMALAT стерилизованное 3,5%, 1л",
                "parmalat-milk-response.html",
                "ME: 1 штука",
                90.49f,
                new HashMap<>(),
                100
        );
        TEST_PRODUCTS[0].getWholesalePrice().put(3, 85.97f);
        TEST_PRODUCTS[0].getWholesalePrice().put(12, 65.9f);

        TEST_PRODUCTS[1] = new MetroProduct(
                373527L,
                "Хлопья овсяные NORDIC, 500г",
                "example-product-response.html",
                "ME: 1 штука",
                119.90f,
                new HashMap<>(),
                10
        );
    }

    @Test
    void supports() {
        assertTrue(converter.supports(MetroProduct.class));
    }

    @Test
    void readInternalFirst() {
        readTest(0);
    }

    @Test
    void readInternalSecond() {
        readTest(1);
    }

    private void readTest(int i) {
        try {
            byte[] bytes = Files.readAllBytes(
                    Paths.get("target/classes/static")
                            .resolve(TEST_PRODUCTS[i].getUrl()));
            HttpInputMessage inputMessage = new MockHttpInputMessage(bytes);

            MetroProduct metroProduct = converter.readInternal(
                    MetroProduct.class,
                    inputMessage
            );

            assertNotNull(metroProduct);
            assertEquals(TEST_PRODUCTS[i].getId(), metroProduct.getId());
            assertEquals(TEST_PRODUCTS[i].getName(), metroProduct.getName());
            assertEquals(TEST_PRODUCTS[i].getRegularPrice(), metroProduct.getRegularPrice());
//            assertEquals(TEST_PRODUCTS[i].getUrl(), metroProduct.getUrl());
            assertEquals(TEST_PRODUCTS[i].getLeftPct(), metroProduct.getLeftPct());
            assertEquals(TEST_PRODUCTS[i].getMe(), metroProduct.getMe());
            assertEquals(TEST_PRODUCTS[i].getWholesalePrice(), metroProduct.getWholesalePrice());
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void writeInternal() {
    }
}