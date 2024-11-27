package candyCo.htmlcontroller;

import candyCo.application.HtmlController;
import candyCo.customer.Customer;
import candyCo.customer.CustomerService;
import candyCo.customeraddress.CustomerAddress;
import candyCo.customeraddress.CustomerAddressService;
import candyCo.order.OrderService;
import candyCo.product.Product;
import candyCo.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(controllers = HtmlController.class)
class HtmlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomerAddressService customerAddressService;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderService orderService;


    private List<Customer> customers;
    private List<CustomerAddress> customerAddresses;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customers = new ArrayList<>();
        customers.add(new Customer("John", "Doe", "john.doe@example.com"));
        customers.add(new Customer("Jane", "Smith", "jane.smith@example.com"));

        customerAddresses = new ArrayList<>();
        customerAddresses.add(new CustomerAddress("123 Main St", "City", "12345", customers.get(0)));
        customerAddresses.add(new CustomerAddress("456 Elm St", "Town", "67890", customers.get(1)));

        products = new ArrayList<>();
        products.add(new Product("Candy", "Sweet candy", BigDecimal.valueOf(5.99), 100, null));
        products.add(new Product("Chocolate", "Dark chocolate", BigDecimal.valueOf(10.99), 50, null));
    }
    @Test
    void testGetAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(customers);
        when(customerAddressService.getAllAddresses()).thenReturn(customerAddresses);

        mockMvc.perform(get("/html"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("customers"))
                .andExpect(model().attributeExists("customerAddresses"))
                .andExpect(model().attribute("customers", hasSize(2)))
                .andExpect(model().attribute("customerAddresses", hasSize(2)));
    }
    @Test
    void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/html/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", hasSize(2)))
                .andExpect(model().attribute("products", hasItem(
                        hasProperty("name", is("Candy"))
                )))
                .andExpect(model().attribute("products", hasItem(
                        hasProperty("name", is("Chocolate"))
                )));
    }
}
