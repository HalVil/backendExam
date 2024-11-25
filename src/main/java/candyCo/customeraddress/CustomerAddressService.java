package candyCo.customeraddress;

import candyCo.application.error.CustomerAddressNotFoundException;
import candyCo.customer.Customer;
import candyCo.customer.CustomerService;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CustomerAddressService {

    private final CustomerAddressRepo customerAddressRepository;
    private final CustomerService customerService;

    public CustomerAddressService(CustomerAddressRepo customerAddressRepository, CustomerService customerService) {
        this.customerAddressRepository = customerAddressRepository;
        this.customerService = customerService;
    }

    public List<CustomerAddress> getAllAddresses() {
        return customerAddressRepository.findAll();
    }

    public CustomerAddress getAddressById(Long id) {
        return customerAddressRepository.findById(id)
                .orElseThrow(() -> new CustomerAddressNotFoundException("Address with ID " + id + " was not found."));
    }
    public CustomerAddress createAddress(CustomerAddress customerAddress) {
        return customerAddressRepository.save(customerAddress);
    }

    public void deleteAddress(Long id) {
        if (!customerAddressRepository.existsById(id)) {
            throw new CustomerAddressNotFoundException("Address with ID " + id + " does not exist.");
        }
        customerAddressRepository.deleteById(id);
    }

    public void deleteAllAddresses() {
        customerAddressRepository.deleteAll();
    }

    public CustomerAddress updateAddress(Long id, CustomerAddressRequest request) {
        CustomerAddress existingAddress = getAddressById(id);

        if (request.getStreet() != null) existingAddress.setStreet(request.getStreet());
        if (request.getCity() != null) existingAddress.setCity(request.getCity());
        if (request.getState() != null) existingAddress.setState(request.getState());
        if (request.getZipCode() != 0) existingAddress.setZipCode(request.getZipCode());

        if (request.getCustomerId() != null) {
            Customer customer = customerService.getCustomerById(request.getCustomerId());
            if (customer == null) {
                throw new IllegalArgumentException("Customer with ID " + request.getCustomerId() + " does not exist.");
            }
            existingAddress.setCustomer(customer);
        }

        return customerAddressRepository.save(existingAddress);
    }


}
