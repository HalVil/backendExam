package candyCo.product;

import candyCo.application.error.ProductNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ProductService {

    private final ProductRepo productRepository;

    public ProductService(ProductRepo productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        // Validering, om nødvendig, kan legges til her
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    public Set<Product> getAvailableProducts(Set<Long> productIds) {
        return productRepository.findByStatus(ProductStatus.AVAILABLE).stream()
                .filter(product -> productIds.contains(product.getId()))
                .collect(Collectors.toSet());
    }

}
