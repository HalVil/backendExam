package candyCo.order;

import candyCo.customer.Customer;
import candyCo.customer.CustomerService;
import candyCo.customeraddress.CustomerAddress;
import candyCo.customeraddress.CustomerAddressService;
import candyCo.product.Product;
import candyCo.product.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


@Service
public class OrderService {

    private final OrderRepo orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final CustomerAddressService customerAddressService;

    public OrderService(OrderRepo orderRepository,
                        CustomerService customerService,
                        ProductService productService,
                        CustomerAddressService customerAddressService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.productService = productService;
        this.customerAddressService = customerAddressService;
    }

    public Order createOrderFromRequest(OrderRequest request) {
        // Hent kunde via CustomerService
        Customer customer = customerService.getCustomerById(request.getCustomerId());

        // Hent tilgjengelige produkter via ProductService
        Set<Product> products = productService.getAvailableProducts(request.getProductIds());

        // Hent shipping address via CustomerAddressService
        CustomerAddress shippingAddress = customerAddressService.getAddressById(request.getShippingAddressId());

        // Opprett ordre
        Order order = new Order();
        order.setShippingCharge(request.getShippingCharge());
        order.setCustomer(customer);
        order.setProduct(products);
        order.setShippingAddress(shippingAddress);
        order.setTotalPrice(calculateTotalPrice(products, request.getShippingCharge()));
        order.setShipped(request.getShipped());

        return orderRepository.save(order);
    }


    private BigDecimal calculateTotalPrice(Set<Product> products, BigDecimal shippingCharge) {
        BigDecimal productTotal = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return productTotal.add(shippingCharge);
    }

    // Other methods remain unchanged


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
    public Set<Product> getOrderProducts(Long id) {
        Order order = getOrderById(id);
        return order.getProduct(); // Antar at getProducts() returnerer en liste
    }
}
