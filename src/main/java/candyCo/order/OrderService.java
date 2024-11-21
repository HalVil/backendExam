package candyCo.order;

import candyCo.product.Product;
import candyCo.product.ProductRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class OrderService {

    private final OrderRepo orderRepository;
    private final ProductRepo productRepository;

    public OrderService(OrderRepo orderRepository,ProductRepo productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(Order order) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Product product : order.getProducts()) {
            Product dbProduct = productRepository.findById(product.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + product.getId()));

            if (dbProduct.getQuantityOnHand() < 1) {
                throw new IllegalArgumentException("Product out of stock: " + dbProduct.getName());
            }

            // Deduct stock and update the product
            dbProduct.setQuantityOnHand(dbProduct.getQuantityOnHand() - 1);
            productRepository.save(dbProduct);

            // Update total price
            totalPrice = totalPrice.add(dbProduct.getPrice());
        }

        order.setTotalPrice(totalPrice.add(order.getShippingCharge()));
        order.setShipped(false); // Default order to not shipped
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
