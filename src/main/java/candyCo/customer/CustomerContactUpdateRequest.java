package candyCo.customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerContactUpdateRequest {
    private String email;
    private int phone;

    public CustomerContactUpdateRequest(String mail, int i) {
        this.email = mail;
        this.phone = i;
    }
}
