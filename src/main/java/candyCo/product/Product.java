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
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_gen")
    @SequenceGenerator(name = "product_gen", sequenceName = "product_seq", allocationSize = 1)
    @Column(name = "product_id", nullable = false)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private int quantityOnHand;

    @Enumerated(EnumType.STRING)
    @Column
    private ProductStatus status;

    public Product(String s, String sentence, BigDecimal bigDecimal, int i, ProductStatus productStatus) {
        this.name = s;
        this.description = sentence;
        this.price = bigDecimal;
        this.quantityOnHand = i;
        this.status = productStatus;

    }
}