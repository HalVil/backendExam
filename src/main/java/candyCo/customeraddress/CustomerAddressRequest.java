package candyCo.customeraddress;

import candyCo.customer.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerAddressRequest {
    private Long customerId;
    private String street;
    private String city;
    private String state;
    private int zipCode;
    private Customer customer;
}