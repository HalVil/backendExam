package candyCo.customeraddress;

import candyCo.application.error.CustomerAddressNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerAddressService {

    private final CustomerAddressRepo customerAddressRepository;

    // Konstruktør for å injisere repository
    public CustomerAddressService(CustomerAddressRepo customerAddressRepository) {
        this.customerAddressRepository = customerAddressRepository;
    }

    // Hent alle kundeadresser
    public List<CustomerAddress> getAllAddresses() {
        return customerAddressRepository.findAll();
    }

    // Hent en kundeadresse basert på ID
    public CustomerAddress getAddressById(Long id) {
        return customerAddressRepository.findById(id)
                .orElseThrow(() -> new CustomerAddressNotFoundException("Address with ID " + id + " was not found."));
    }

    // Opprett en ny kundeadresse
    public CustomerAddress createAddress(CustomerAddress customerAddress) {
        return customerAddressRepository.save(customerAddress);
    }

    // Slett en kundeadresse basert på ID
    public void deleteAddress(Long id) {
        if (!customerAddressRepository.existsById(id)) {
            throw new CustomerAddressNotFoundException("Address with ID " + id + " does not exist.");
        }
        customerAddressRepository.deleteById(id);
    }
}
