package candyCo.order;

import candyCo.customer.Customer;
import candyCo.customer.CustomerService;
import candyCo.customeraddress.CustomerAddress;
import candyCo.customeraddress.CustomerAddressService;
import candyCo.product.Product;
import candyCo.product.ProductService;
import candyCo.product.ProductStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        // Hent kunde
        Customer customer = customerService.getCustomerById(request.getCustomerId());

        // Hent produkter og opprett OrderProduct-objekter
        Set<OrderProduct> orderProducts = new HashSet<>();
        for (Map.Entry<Long, Integer> entry : request.getProductsWithQuantities().entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            // Hent produkt og valider status
            Product product = productService.getProductById(productId);
            if (product.getStatus() != ProductStatus.AVAILABLE) {
                throw new IllegalArgumentException("Product is not available: " + product.getName());
            }

            // Opprett OrderProduct
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProduct(product);
            orderProduct.setQuantity(quantity);
            orderProducts.add(orderProduct);
        }

        // Opprett Order
        CustomerAddress shippingAddress = customerAddressService.getAddressById(request.getShippingAddressId());
        Order order = new Order();
        order.setCustomer(customer);
        order.setShippingAddress(shippingAddress);
        order.setShipped(request.getShipped());
        order.setShippingCharge(request.getShippingCharge());
        order.setOrderProducts(orderProducts);

        // Koble Order til OrderProduct
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.setOrder(order); // Viktig for å sette relasjonen
        }

        // Beregn totalpris
        order.setTotalPrice(calculateTotalPrice(orderProducts, request.getShippingCharge()));

        // Lagre Order (OrderProduct blir automatisk lagret pga. CascadeType.PERSIST)
        return orderRepository.save(order);
    }




    private BigDecimal calculateTotalPrice(Set<OrderProduct> orderProducts, BigDecimal shippingCharge) {
        BigDecimal productTotal = orderProducts.stream()
                .map(orderProduct -> orderProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(orderProduct.getQuantity())))
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

    //fjernet midlertidig
}
