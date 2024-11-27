package candyCo.order;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderProductIdTest {

    @Test
    void testGetterAndSetter() {
        OrderProductId orderProductId = new OrderProductId();

        orderProductId.setOrderId(1L);
        orderProductId.setProductId(2L);

        assertThat(orderProductId.getOrderId()).isEqualTo(1L);
        assertThat(orderProductId.getProductId()).isEqualTo(2L);
    }
    @Test
    void testEquals() {
        OrderProductId id1 = new OrderProductId();
        id1.setOrderId(1L);
        id1.setProductId(2L);

        OrderProductId id2 = new OrderProductId();
        id2.setOrderId(1L);
        id2.setProductId(2L);

        assertThat(id1).isEqualTo(id2);

        id2.setProductId(3L);
        assertThat(id1).isNotEqualTo(id2);
    }
    @Test
    void testHashCode() {
        OrderProductId id1 = new OrderProductId();
        id1.setOrderId(1L);
        id1.setProductId(2L);

        OrderProductId id2 = new OrderProductId();
        id2.setOrderId(1L);
        id2.setProductId(2L);

        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());

        id2.setProductId(3L);
        assertThat(id1.hashCode()).isNotEqualTo(id2.hashCode());
    }
}
