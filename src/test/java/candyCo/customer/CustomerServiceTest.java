package candyCo.customer;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerServiceTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:alpine");

    @Autowired
    CustomerService customerService;

    @Test
    @Order(1)
    void setup() {
        customerService.deleteAllCustomers();

        for (int i = 0; i < 10; i++) {
            customerService.createCustomer(new Customer("Customer " + i, "LastName " + i, 1234567890 + i, "email" + i + "@example.com"));
        }
    }
    @Test
    @Order(3)
    void testCreateCustomer() {
        Customer newCustomer = new Customer("John", "Doe", 987654321, "john.doe@example.com");
        Customer savedCustomer = customerService.createCustomer(newCustomer);

        assertEquals("John", savedCustomer.getFirstName(), "Customer's first name should match.");
    }
    @Test
    @Order(2)
    void testGetAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        assertEquals(10, customers.size(), "There should be 10 customers in the database.");
    }
    @Test
    @Order(4)
    void testDeleteAllCustomers() {
        customerService.deleteAllCustomers();

        List<Customer> customers = customerService.getAllCustomers();
        assertEquals(0, customers.size(), "All customers should be deleted.");
    }
}