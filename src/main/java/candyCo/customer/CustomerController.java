package candyCo.customer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.createCustomer(customer), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerDetails(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        customer.getAddresses().size();
        customer.getOrders().size();
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllCustomers() {
        customerService.deleteAllCustomers();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/contact")
    public ResponseEntity<Customer> updateCustomerContact(
            @PathVariable Long id,
            @RequestBody CustomerContactUpdateRequest request) {
        Customer updatedCustomer = customerService.updateCustomerContact(id, request.getEmail(), request.getPhone());
        return ResponseEntity.ok(updatedCustomer);
    }





}
