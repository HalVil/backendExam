package candyCo;

import candyCo.customeraddress.CustomerAddress;
import candyCo.product.Product;
import candyCo.product.ProductStatus;
import com.github.javafaker.Faker;
import candyCo.customer.Customer;
import candyCo.customer.CustomerService;
import candyCo.customeraddress.CustomerAddressService;
import candyCo.order.OrderService;
import candyCo.product.ProductService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service

public class InitData {

    private final CustomerService customerService;
    private final CustomerAddressService customerAddressService;
    private final ProductService productService;
    private final Faker faker = Faker.instance();
    private String generateEmail(String firstName, String lastName) {
        String formattedFirstName = firstName.toLowerCase().replaceAll("[^a-z]", "");
        String formattedLastName = lastName.toLowerCase().replaceAll("[^a-z]", "");
        return formattedFirstName + "." + formattedLastName + "@example.com";
    }
    @Autowired
    public InitData(CustomerService customerService, CustomerAddressService customerAddressService, OrderService orderService, ProductService productService) {
        this.customerService = customerService;
        this.customerAddressService = customerAddressService;
        this.productService = productService;
    }
    @PostConstruct
    public void createTestData() {

        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            int randomPhoneNumber = faker.number().numberBetween(10000000, 99999999);

            String email = generateEmail(firstName, lastName);

            customers.add(
                    customerService.createCustomer(
                            new Customer(
                                    firstName,
                                    lastName,
                                    randomPhoneNumber,
                                    email
                            )
                    )
            );
        }

        customers.forEach(customer -> {
            for (int i = 0; i < 1; i++) {
                customerAddressService.createAddress(
                        new CustomerAddress(
                                faker.address().streetAddress(),
                                faker.address().city(),
                                faker.address().state(),
                                faker.number().numberBetween(10000, 99999),
                                customer
                        )

                );
            }
        });

        List<Product> products = new ArrayList<>();

        Map<String, String> candyProducts = Map.ofEntries(
                Map.entry("Caramel Delight", "A delightful burst of sweetness in every bite."),
                Map.entry("Choco Crunch", "Rich and creamy with a crunchy twist."),
                Map.entry("Mint Bliss", "Refreshing mint flavor that melts in your mouth."),
                Map.entry("Berry Chew", "Juicy berry flavors with a chewy texture."),
                Map.entry("Fudge Twist", "Smooth fudge with a hint of caramel."),
                Map.entry("Lollipop Bliss", "A swirl of fruity goodness on a stick."),
                Map.entry("Taffy Swirl", "Stretchy, chewy candy with bold flavors."),
                Map.entry("Sugar Drops", "Tiny, colorful candies that pack a sweet punch."),
                Map.entry("Citrus Burst", "A zesty and tangy treat."),
                Map.entry("Candy Clouds", "Soft, fluffy, and melts like magic."),
                Map.entry("Vanilla Creams", "Classic vanilla with a creamy finish."),
                Map.entry("Choco Minty", "Perfect blend of chocolate and mint."),
                Map.entry("Strawberry Swizzle", "Sweet strawberry flavor with a hint of sour."),
                Map.entry("Rainbow Bites", "A colorful explosion of fruity flavors."),
                Map.entry("Peanut Butter Cups", "Rich peanut butter encased in smooth chocolate.")
        );

        for (Map.Entry<String, String> entry : candyProducts.entrySet()) {
            boolean isAvailable = faker.random().nextBoolean();
            ProductStatus status = isAvailable ? ProductStatus.AVAILABLE : ProductStatus.OUT_OF_STOCK;
            int quantityOnHand = isAvailable ? faker.number().numberBetween(1, 100) : 0;

            products.add(
                    productService.createProduct(
                            new Product(
                                    entry.getKey(),
                                    entry.getValue(),
                                    new BigDecimal(faker.commerce().price(5.0, 100.0).replace(",", ".")),
                                    quantityOnHand,
                                    status
                            )
                    )
            );
        }
    }
}
