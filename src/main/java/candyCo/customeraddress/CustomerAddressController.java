package candyCo.customeraddress;

import candyCo.customer.Customer;
import candyCo.customer.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<List<CustomerAddress>> getAllAddresses() {
        List<CustomerAddress> addresses = customerAddressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerAddress> getAddressById(@PathVariable Long id) {
        CustomerAddress address = customerAddressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }

    @PostMapping
    public ResponseEntity<CustomerAddress> createAddress(@RequestBody CustomerAddressRequest request) {
        Customer customer = customerService.getCustomerById(request.getCustomerId());


        if (customer == null) {
            return ResponseEntity.badRequest().body(null);
        }

        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setStreet(request.getStreet());
        customerAddress.setCity(request.getCity());
        customerAddress.setState(request.getState());
        customerAddress.setZipCode(request.getZipCode());
        customerAddress.setCustomer(customer);

        CustomerAddress createdAddress = customerAddressService.createAddress(customerAddress);
        return ResponseEntity.status(201).body(createdAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        customerAddressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllAddresses() {
        customerAddressService.deleteAllAddresses();
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<CustomerAddress> updateAddress(
            @PathVariable Long id,
            @RequestBody CustomerAddressRequest request) {
        CustomerAddress updatedAddress = customerAddressService.updateAddress(id, request);
        return ResponseEntity.ok(updatedAddress);
    }

}