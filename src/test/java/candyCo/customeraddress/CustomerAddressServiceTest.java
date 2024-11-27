package candyCo.customeraddress;

import candyCo.customer.Customer;
import candyCo.customer.CustomerService;
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
class CustomerAddressServiceTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:alpine");

    @Autowired
    CustomerAddressService customerAddressService;

    @Autowired
    CustomerService customerService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        customerService.deleteAllCustomers();
        customerAddressService.deleteAllAddresses();
        testCustomer = customerService.createCustomer(new Customer("John", "Doe", 987654321, "john.doe@example.com"));
    }
    @Test
    @Order(1)
    void testCreateAddress() {
        CustomerAddress newAddress = new CustomerAddress("123 Main St", "Anytown", "Anystate", 12345, testCustomer);
        CustomerAddress savedAddress = customerAddressService.createAddress(newAddress);

        assertNotNull(savedAddress.getId(), "Address ID should be generated.");
        assertEquals("123 Main St", savedAddress.getStreet(), "Address street should match.");
    }
    @Test
    @Order(2)
    void testGetAllAddresses() {
        for (int i = 0; i < 5; i++) {
            customerAddressService.createAddress(new CustomerAddress("Street " + i, "City " + i, "State " + i, 10000 + i, testCustomer));
        }

        List<CustomerAddress> addresses = customerAddressService.getAllAddresses();
        assertEquals(5, addresses.size(), "There should be 5 addresses in the database.");
    }
    @Test
    @Order(3)
    void testGetAddressById() {
        CustomerAddress address = customerAddressService.createAddress(new CustomerAddress("456 Oak St", "Somewhere", "AnyState", 54321, testCustomer));

        CustomerAddress fetchedAddress = customerAddressService.getAddressById(address.getId());
        assertNotNull(fetchedAddress, "Address should be found by ID.");
        assertEquals("456 Oak St", fetchedAddress.getStreet(), "Street of the fetched address should match.");
    }
    @Test
    @Order(4)
    void testDeleteAddress() {
        CustomerAddress address = customerAddressService.createAddress(new CustomerAddress("789 Pine St", "Elsewhere", "OtherState", 67890, testCustomer));

        customerAddressService.deleteAddress(address.getId());

        List<CustomerAddress> addresses = customerAddressService.getAllAddresses();
        assertFalse(addresses.contains(address), "Address should be deleted.");
    }
    @Test
    @Order(5)
    void testDeleteAllAddresses() {
        for (int i = 0; i < 3; i++) {
            customerAddressService.createAddress(new CustomerAddress("Street " + i, "City " + i, "State " + i, 20000 + i, testCustomer));
        }

        customerAddressService.deleteAllAddresses();

        List<CustomerAddress> addresses = customerAddressService.getAllAddresses();
        assertEquals(0, addresses.size(), "All addresses should be deleted.");
    }
    @Test
    @Order(6)
    void testUpdateAddress() {
        CustomerAddress address = customerAddressService.createAddress(new CustomerAddress("123 Old St", "Oldtown", "Oldstate", 11111, testCustomer));

        CustomerAddressRequest updateRequest = new CustomerAddressRequest();
        updateRequest.setStreet("123 New St");
        updateRequest.setCity("Newtown");
        updateRequest.setState("Newstate");
        updateRequest.setZipCode(22222);
        updateRequest.setCustomerId(testCustomer.getId());

        CustomerAddress updatedAddress = customerAddressService.updateAddress(address.getId(), updateRequest);

        assertEquals("123 New St", updatedAddress.getStreet(), "Updated street should match.");
        assertEquals("Newtown", updatedAddress.getCity(), "Updated city should match.");
        assertEquals("Newstate", updatedAddress.getState(), "Updated state should match.");
        assertEquals(22222, updatedAddress.getZipCode(), "Updated zip code should match.");
    }
}
