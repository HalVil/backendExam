package candyCo.order;

import candyCo.customer.Customer;
import candyCo.customer.CustomerService;
import candyCo.customeraddress.CustomerAddress;
import candyCo.customeraddress.CustomerAddressService;
import candyCo.product.Product;
import candyCo.product.ProductService;
import candyCo.application.error.OutOfStockException;
import candyCo.product.ProductStatus;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class OrderServiceTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:alpine");

    @Autowired
    OrderService orderService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ProductService productService;

    @Autowired
    CustomerAddressService customerAddressService;

    @Autowired
    private EntityManager entityManager;

    private Customer testCustomer;
    private CustomerAddress testShippingAddress;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        customerService.deleteAllCustomers();
        productService.deleteAllProducts();
        customerAddressService.deleteAllAddresses();

        testCustomer = customerService.createCustomer(new Customer("John", "Doe", 987654321, "john.doe@example.com"));

        testProduct = new Product();
        testProduct.setName("Candy Bar");
        testProduct.setPrice(BigDecimal.valueOf(10));
        testProduct.setQuantityOnHand(100);
        testProduct.setStatus(ProductStatus.AVAILABLE);
        testProduct = productService.createProduct(testProduct);

        testProduct = entityManager.find(Product.class, testProduct.getId());

        testShippingAddress = new CustomerAddress("123 Main St", "Anytown", "Anystate", 12345, testCustomer);
        testShippingAddress = customerAddressService.createAddress(testShippingAddress);
    }
    @Test
    void testCreateOrderWithOutOfStockProduct() {
        Product outOfStockProduct = new Product();
        outOfStockProduct.setName("Gummy Bears");
        outOfStockProduct.setPrice(BigDecimal.valueOf(5));
        outOfStockProduct.setQuantityOnHand(1);
        outOfStockProduct.setStatus(ProductStatus.AVAILABLE);
        outOfStockProduct = productService.createProduct(outOfStockProduct);

        Map<Long, Integer> productsWithQuantities = new HashMap<>();
        productsWithQuantities.put(outOfStockProduct.getId(), 2);

        OrderRequest request = new OrderRequest();
        request.setShippingCharge(BigDecimal.valueOf(5));
        request.setCustomerId(testCustomer.getId());
        request.setProductsWithQuantities(productsWithQuantities);
        request.setShippingAddressId(testShippingAddress.getId());
        request.setShipped(false);

        assertThrows(OutOfStockException.class, () -> orderService.createOrderFromRequest(request),
                "Expected OutOfStockException when trying to order more than available stock.");
    }
    @Test
    void testGetAllOrders() {
        Map<Long, Integer> productsWithQuantities = new HashMap<>();
        productsWithQuantities.put(testProduct.getId(), 10);

        OrderRequest request = new OrderRequest();
        request.setShippingCharge(BigDecimal.valueOf(5));
        request.setCustomerId(testCustomer.getId());
        request.setProductsWithQuantities(productsWithQuantities);
        request.setShippingAddressId(testShippingAddress.getId());
        request.setShipped(false);

        Order createdOrder = orderService.createOrderFromRequest(request);

        List<Order> allOrders = orderService.getAllOrders();

        assertNotNull(allOrders, "The list of orders should not be null.");
        assertFalse(allOrders.isEmpty(), "The list of orders should contain at least one order.");

        assertTrue(allOrders.contains(createdOrder), "The created order should be in the list of all orders.");
    }
}
