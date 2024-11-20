package candyCo.customeraddress;

import candyCo.customer.Customer;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "customerAddress_gen")
    @SequenceGenerator(name = "customerAddress_gen", sequenceName = "customerAddress_seq", allocationSize = 1)
    @Column(name = "customerAddress_id", nullable = false)
    private Long id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    //flere adresser til en kunde, oppgaven sier så
    @ManyToOne(fetch = FetchType.LAZY) //forsinker lasting fra db til kun når kalt på
    private Customer customer;

    // denne kalles når initData kjøres
    public CustomerAddress(String street, String city, String state, String zipCode, Customer customer) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.customer = customer;
    }
}
