package candyCo.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUpdateRequest {
    private Boolean shipped;
    private Long shippingAddressId;
}
