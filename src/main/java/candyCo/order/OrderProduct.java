package candyCo.order;

import candyCo.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OrderProduct {

    @EmbeddedId
    private OrderProductId id = new OrderProductId();

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "candy_order_id")
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;


}