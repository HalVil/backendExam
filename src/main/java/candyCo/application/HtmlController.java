package candyCo.application;


import candyCo.customer.Customer;
import candyCo.customeraddress.CustomerAddress;
import candyCo.customeraddress.CustomerAddressService;
import candyCo.order.Order;
import candyCo.order.OrderService;
import candyCo.product.Product;
import candyCo.customer.CustomerService;
import candyCo.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/html")
public class HtmlController {
    private final CustomerService customerService;
    private final ProductService productService;
    private final CustomerAddressService customerAddressService;
    private final OrderService orderService;


    @Autowired
    public HtmlController(CustomerService customerService, ProductService productService, CustomerAddressService customerAddressService, OrderService orderService) {
        this.customerService = customerService;
        this.productService = productService;
        this.customerAddressService = customerAddressService;
        this.orderService = orderService;
    }

    @GetMapping
    public String getAllCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerAddress> customerAddresses = customerAddressService.getAllAddresses();
        System.out.println("Customers size: " + customers.size());
        model.addAttribute("customers", customers);
        System.out.println("Customers address size: " + customerAddresses.size());
        model.addAttribute("customerAddresses", customerAddresses);
        return "index";
    }
    @GetMapping("/products")
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }
    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        orders.forEach(order -> System.out.println("Order ID: " + order.getId()));
        model.addAttribute("orders", orders);
        return "orders";
    }


}

