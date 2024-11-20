package candyCo.customer;

import candyCo.application.error.CustomerNotFoundException;
import candyCo.customeraddress.CustomerAddress;
import candyCo.order.Order;
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
                .orElseThrow(() -> new CustomerNotFoundException("Address with ID " + id + " was not found."));
    }

    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }

    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        repo.delete(customer);
    }

    public List<Order> getCustomerOrders(Long customerId) {
        Customer customer = getCustomerById(customerId);
        return customer.getOrders();
    }

    public List<CustomerAddress> getCustomerAddresses(Long customerId) {
        Customer customer = getCustomerById(customerId);
        return customer.getAddresses();
    }
}
