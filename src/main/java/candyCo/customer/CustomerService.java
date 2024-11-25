package candyCo.customer;

import candyCo.application.error.CustomerNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepo repo;


    public CustomerService(CustomerRepo customerRepository) {
        this.repo = customerRepository;

    }

    public Customer createCustomer(Customer customer) {
        return repo.save(customer);
    }

    public Customer getCustomerById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " was not found."));
    }

    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }

    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        repo.delete(customer);
    }
    public void deleteAllCustomers() {
        repo.deleteAll();
    }

    public Customer updateCustomerContact(Long id, String email, int phone) {
        Customer customer = getCustomerById(id);

        if (email != null && !email.isEmpty()) {
            customer.setEmail(email);
        }
        if (phone > 0) {
            customer.setPhone(phone);
        }

        return repo.save(customer);
    }

}
