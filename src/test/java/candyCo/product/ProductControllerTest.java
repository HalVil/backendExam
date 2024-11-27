package candyCo.product;

import candyCo.application.error.ProductNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    static List<Product> products = new ArrayList<>();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @BeforeAll
    static void setUpBeforeClass() {
        for (int i = 0; i < 10; i++) {
            products.add(new Product(
                    "Product " + i,
                    "Description for product " + i,
                    BigDecimal.valueOf(10.99 + i),
                    100 - i,
                    ProductStatus.AVAILABLE
            ));
        }
    }
    @Test
    void getAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(products);

        this.mockMvc.perform(get("/api/product"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product 0"));
    }
    @Test
    void deleteProduct() throws Exception {
        this.mockMvc.perform(delete("/api/product/1"))
                .andExpect(status().isNoContent());
    }
    @Test
    void deleteAllProducts() throws Exception {
        this.mockMvc.perform(delete("/api/product/deleteAll"))
                .andExpect(status().isNoContent());
    }
    @Test
    void createProduct() throws Exception {
        var newProduct = new Product(
                "New Product",
                "New product description",
                BigDecimal.valueOf(15.99),
                50,
                ProductStatus.AVAILABLE
        );

        when(productService.createProduct(any(Product.class))).thenReturn(newProduct);

        this.mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newProduct)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.price").value(15.99));
    }
    @Test
    void getProductById() throws Exception {
        var product = new Product(
                "Product 1",
                "Description for product 1",
                BigDecimal.valueOf(11.99),
                99,
                ProductStatus.AVAILABLE
        );

        when(productService.getProductById(1L)).thenReturn(product);

        this.mockMvc.perform(get("/api/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product 1"))
                .andExpect(jsonPath("$.price").value(11.99));
    }
    @Test
    void getProductById_ProductNotFound() throws Exception {
        when(productService.getProductById(99L)).thenThrow(new ProductNotFoundException("Product with ID 99 not found"));

        this.mockMvc.perform(get("/api/product/99"))
                .andExpect(status().isNotFound());

    }
    @Test
    void getAvailableProducts() throws Exception {
        when(productService.getAvailableProducts()).thenReturn(products);

        this.mockMvc.perform(get("/api/product/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"));
    }
}
