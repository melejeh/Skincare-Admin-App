package PlanetMel.service;

import PlanetMel.domain.Product;
import PlanetMel.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Add a new product
    public Product addProduct(String name, String description, double price, String category, String imageUrl) {
        Product product = new Product(name, description, price, category, imageUrl);
        return productRepository.save(product);
    }

    // Delete a product by ID
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Update an existing product
    public Product updateProduct(Long id, String name, String description, double price, String category, String imageUrl) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setImageUrl(imageUrl);
        return productRepository.save(product);
    }
}
