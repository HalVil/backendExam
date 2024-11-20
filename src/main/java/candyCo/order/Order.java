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
@Table(name = "candy_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candy_order_gen")
    @SequenceGenerator(name = "candy_order_gen", sequenceName = "candy_order_seq", allocationSize = 1)
    @Column(name = "candy_order_id", nullable = false)
    private Long id;

    private BigDecimal shippingCharge;
    private BigDecimal totalPrice;
    private Boolean shipped;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "candy_order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private CustomerAddress shippingAddress;
}