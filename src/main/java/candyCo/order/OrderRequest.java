package candyCo.order;

import candyCo.customeraddress.CustomerAddress;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class OrderRequest {
    private BigDecimal shippingCharge;
    private Long customerId;
    private Set<Long> productIds;
    private Long shippingAddressId;
    private Boolean shipped;


}
