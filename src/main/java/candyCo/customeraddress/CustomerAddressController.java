package candyCo.customeraddress;

import candyCo.customer.Customer;
import candyCo.customer.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/customer-addresses")
public class CustomerAddressController {

    private final CustomerAddressService customerAddressService;
    private final CustomerService customerService;


    public CustomerAddressController(CustomerAddressService customerAddressService, CustomerService customerService) {
        this.customerAddressService = customerAddressService;
        this.customerService = customerService;

    }

    // Hent alle kundeadresser
    @GetMapping
    public ResponseEntity<List<CustomerAddress>> getAllAddresses() {
        List<CustomerAddress> addresses = customerAddressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    // Hent en spesifikk kundeadresse basert på ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerAddress> getAddressById(@PathVariable Long id) {
        CustomerAddress address = customerAddressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }

    // Opprett en ny kundeadresse
    @PostMapping
    public ResponseEntity<CustomerAddress> createAddress(@RequestBody CustomerAddressRequest request) {
        // Fetch the customer based on customerId
        Customer customer = customerService.getCustomerById(request.getCustomerId());


        if (customer == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // Set the customer to the address
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setStreet(request.getStreet());
        customerAddress.setCity(request.getCity());
        customerAddress.setState(request.getState());
        customerAddress.setZipCode(request.getZipCode());
        customerAddress.setCustomer(customer);  // Associate the address with the customer

        CustomerAddress createdAddress = customerAddressService.createAddress(customerAddress);
        return ResponseEntity.status(201).body(createdAddress);
    }

    // Slett en kundeadresse
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        customerAddressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}