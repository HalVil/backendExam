package candyCo.order;

import candyCo.customer.CustomerService;
import candyCo.product.Product;
import candyCo.product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;


    }

    // Opprett en ny ordre
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        Order order = orderService.createOrderFromRequest(request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }


    // Hent en ordre basert på ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }

    // Hent alle ordrer
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    // Slett en ordre basert på ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Hent alle produkter tilknyttet en ordre
    @GetMapping("/{id}/products")
    public ResponseEntity<Set<Product>> getOrderProducts(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.getOrderProducts(id), HttpStatus.OK);
    }
}
