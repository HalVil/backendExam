package candyCo.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(nullable = false)
    private int quantityOnHand;

    // denne kalles når initData kjøres
    public Product(String name, String description, BigDecimal price, ProductStatus status, int quantityOnHand) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.quantityOnHand = quantityOnHand;
    }
}