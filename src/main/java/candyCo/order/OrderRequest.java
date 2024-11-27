package candyCo.order;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class OrderRequest {
    private BigDecimal shippingCharge;
    private Long customerId;
    private Map<Long, Integer> productsWithQuantities;
    private Long shippingAddressId;
    private Boolean shipped;
}
