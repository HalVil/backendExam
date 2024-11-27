package candyCo.product;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductServiceTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:alpine");

    @Autowired
    ProductService productService;

    @Test
    @Order(1)
    void setup() {
        productService.deleteAllProducts();

        for (int i = 0; i < 10; i++) {
            productService.createProduct(new Product(
                    "Product " + i,
                    "Description for product " + i,
                    BigDecimal.valueOf(10.99 + i),
                    100 - i,
                    ProductStatus.AVAILABLE
            ));
        }
    }
    @Test
    @Order(2)
    void testGetAllProducts() {
        List<Product> products = productService.getAllProducts();
        assertEquals(10, products.size(), "There should be 10 products in the database.");
    }
    @Test
    @Order(3)
    void testCreateProduct() {
        Product newProduct = new Product(
                "New Product",
                "New product description",
                BigDecimal.valueOf(15.99),
                50,
                ProductStatus.AVAILABLE
        );

        Product savedProduct = productService.createProduct(newProduct);

        assertEquals("New Product", savedProduct.getName(), "Product's name should match.");
        assertEquals(BigDecimal.valueOf(15.99), savedProduct.getPrice(), "Product's price should match.");
    }
    @Test
    @Order(4)
    void testGetAvailableProducts() {
        List<Product> availableProducts = productService.getAvailableProducts();
        assertEquals(11, availableProducts.size(), "All products should be available.");
    }
    @Test
    @Order(5)
    void testDeleteAllProducts() {
        productService.deleteAllProducts();

        List<Product> products = productService.getAllProducts();
        assertEquals(0, products.size(), "All products should be deleted.");
    }
}
