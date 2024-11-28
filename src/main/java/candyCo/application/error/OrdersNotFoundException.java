package candyCo.application.error;

public class OrdersNotFoundException extends RuntimeException {
    public OrdersNotFoundException(String message) {
        super(message);
    }
}
