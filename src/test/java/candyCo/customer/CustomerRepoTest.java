package candyCo.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LocationRepoTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:alpine");

    @Autowired
    CustomerRepo customerRepo;

    @Test
    void connectionEstablished(){
        assert postgres.isCreated();
        assert postgres.isRunning();
    }
    @Test
    void shouldInsertCustomer(){
        var customer = customerRepo.save(new Customer("Customer 100"));
        assert customerRepo.findById(customer.getId()).isPresent();
    }
    @Test
    void shouldDeleteCustomer() {
        var customer = customerRepo.save(new Customer("Customer 100", "LastName 100", 95866672, "email100@example.com"));

        customerRepo.delete(customer);

        assert customerRepo.findById(customer.getId()).isEmpty();
    }

    @Test
    void shouldFindAndUpdateCustomer() {
        var customer = customerRepo.save(new Customer("Customer 100", "LastName 100", 95866672, "email100@example.com"));

        customer.setEmail("newemail@example.com");
        customer.setPhone(12345678);
        customerRepo.save(customer);

        var updatedCustomer = customerRepo.findById(customer.getId()).orElseThrow();
        assertEquals("newemail@example.com", updatedCustomer.getEmail(), "E-posten skal være oppdatert.");
        assertEquals(12345678, updatedCustomer.getPhone(), "Telefonnummeret skal være oppdatert.");
    }
}
