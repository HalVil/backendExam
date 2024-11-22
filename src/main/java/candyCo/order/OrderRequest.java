package candyCo.order;

import candyCo.customeraddress.CustomerAddress;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class OrderRequest {
    private BigDecimal shippingCharge;
    private Long customerId;
    private Map<Long, Integer> productsWithQuantities;  //lagt til og slått sammen med product id og quantity
    private Long shippingAddressId;
    private Boolean shipped;


}
