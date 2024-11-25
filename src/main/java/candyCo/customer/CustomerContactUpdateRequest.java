package candyCo.customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerContactUpdateRequest {
    private String email;
    private int phone;
}
