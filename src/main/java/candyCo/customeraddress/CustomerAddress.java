package candyCo.customeraddress;

import candyCo.customer.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customer_addresses")
public class CustomerAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_addresses_gen")
    @SequenceGenerator(name = "customer_addresses_gen", sequenceName = "customer_addresses_seq", allocationSize = 1)
    @Column(name = "customer_addresses_id", nullable = false)
    private Long id;

    private String street;
    private String city;
    private String state;
    private int zipCode;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    public CustomerAddress(String s, String city, String state, int i, Customer customer) {
        this.street = s;
        this.city = city;
        this.state = state;
        this.zipCode = i;
        this.customer = customer;
    }
    public CustomerAddress(String s, String city, String number, Customer customer) {
    }
}
