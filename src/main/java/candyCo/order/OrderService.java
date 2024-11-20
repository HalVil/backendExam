package candyCo.order;

import candyCo.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderService {

    private final OrderRepo orderRepository;

    public OrderService(OrderRepo orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Opprett en ny ordre
    public Order createOrder(Order order) {
        // Eventuell forretningslogikk før lagring
        return orderRepository.save(order);
    }

    // Hent en ordre basert på ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
    }

    // Hent alle ordrer
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Slett en ordre basert på ID
    public void deleteOrder(Long id) {
        Order existingOrder = getOrderById(id);
        orderRepository.delete(existingOrder);
    }

    // Hent produkter i en ordre
    public List<Product> getOrderProducts(Long id) {
        Order order = getOrderById(id);
        return (List<Product>) order.getProducts(); // Antar at getProducts() returnerer en liste
    }
}
