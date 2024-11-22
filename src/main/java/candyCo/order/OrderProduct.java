package candyCo.order;

import candyCo.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OrderProduct {

    @EmbeddedId
    private OrderProductId id = new OrderProductId();

    @ManyToOne
    @MapsId("orderId") // Knytter til orderId i sammensatt nøkkel
    @JoinColumn(name = "candy_order_id")
    private Order order;

    @ManyToOne
    @MapsId("productId") // Knytter til productId i sammensatt nøkkel
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;


}