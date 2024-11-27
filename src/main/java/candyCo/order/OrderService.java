package candyCo.order;

import candyCo.application.error.OutOfStockException;
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
        Customer customer = customerService.getCustomerById(request.getCustomerId());

        Set<OrderProduct> orderProducts = new HashSet<>();
        for (Map.Entry<Long, Integer> entry : request.getProductsWithQuantities().entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            Product product = productService.getProductById(productId);
            if (product.getStatus() != ProductStatus.AVAILABLE) {
                throw new OutOfStockException("Product is not available: " + product.getName());
            }
            if (product.getQuantityOnHand() < quantity) {
                throw new OutOfStockException(
                        "Not enough stock for product: " + product.getName() +
                                ". Available: " + product.getQuantityOnHand() + ", Requested: " + quantity);
            }
            product.setQuantityOnHand(product.getQuantityOnHand() - quantity);
            if (product.getQuantityOnHand() == 0) {
                product.setStatus(ProductStatus.OUT_OF_STOCK);
            }
            productService.createProduct(product);

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProduct(product);
            orderProduct.setQuantity(quantity);
            orderProducts.add(orderProduct);
        }
        CustomerAddress shippingAddress = customerAddressService.getAddressById(request.getShippingAddressId());

        Order order = new Order();
        order.setCustomer(customer);
        order.setShippingAddress(shippingAddress);
        order.setShipped(request.getShipped());
        order.setShippingCharge(request.getShippingCharge());
        order.setOrderProducts(orderProducts);

        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.setOrder(order);
        }
        order.setTotalPrice(calculateTotalPrice(orderProducts, request.getShippingCharge()));
        return orderRepository.save(order);
    }
    public Order updateOrder(Long orderId, OrderUpdateRequest updateRequest) {

        Order existingOrder = getOrderById(orderId);

        if (updateRequest.getShipped() != null) {
            existingOrder.setShipped(updateRequest.getShipped());
        }
        if (updateRequest.getShippingAddressId() != null) {
            CustomerAddress newAddress = customerAddressService.getAddressById(updateRequest.getShippingAddressId());
            existingOrder.setShippingAddress(newAddress);
        }

        return orderRepository.save(existingOrder);
    }

    private BigDecimal calculateTotalPrice(Set<OrderProduct> orderProducts, BigDecimal shippingCharge) {
        BigDecimal productTotal = orderProducts.stream()
                .map(orderProduct -> orderProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(orderProduct.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return productTotal.add(shippingCharge);
    }
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrder(Long id) {
        Order existingOrder = getOrderById(id);
        orderRepository.delete(existingOrder);
    }
    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }
}
