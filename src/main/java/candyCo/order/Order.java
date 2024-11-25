package candyCo.order;

import candyCo.customer.Customer;
import candyCo.customeraddress.CustomerAddress;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonIgnoreProperties({"orders", "addresses"})
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("order")
    private Set<OrderProduct> orderProducts = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    @JsonIgnoreProperties("orders")
    private CustomerAddress shippingAddress;
}