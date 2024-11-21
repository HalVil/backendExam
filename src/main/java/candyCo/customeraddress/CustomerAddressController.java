package candyCo.customeraddress;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/customer-addresses")
public class CustomerAddressController {

    private final CustomerAddressService customerAddressService;


    public CustomerAddressController(CustomerAddressService customerAddressService) {
        this.customerAddressService = customerAddressService;
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
    public ResponseEntity<CustomerAddress> createAddress(@RequestBody CustomerAddress customerAddress) {
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