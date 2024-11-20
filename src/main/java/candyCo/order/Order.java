package candyCo.order;

import candyCo.customer.Customer;
import candyCo.customeraddress.CustomerAddress;

import candyCo.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //flere ordre til samme kunde
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    //knytter flere produkter til en ordre
    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    @Column(nullable = false)
    private BigDecimal shippingCharge;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private Boolean shipped;

    //en ordre kan kun ha en leveringsadresse
    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private CustomerAddress shippingAddress;

    // denne kalles når initData kjøres
    public Order (Customer customer, Set<Product> products, BigDecimal shippingCharge) {
        this.customer = customer;
        this.products = products;
        this.shippingCharge = shippingCharge;
        this.shipped = false;
        this.shippingAddress = null;

    }
}