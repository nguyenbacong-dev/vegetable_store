// File: src/main/java/vnua/fita/tthieu/springboot/service/ProductService.java
package vnua.fita.tthieu.springboot.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vnua.fita.tthieu.springboot.entity.Category;
import vnua.fita.tthieu.springboot.entity.Product;
import vnua.fita.tthieu.springboot.repository.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        List<Product> result = productRepository.findAll();
        logger.info("📦 Tất cả sản phẩm: {} items", result.size());
        return result;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product newProduct) {
        Product product = getProductById(id);
        product.setName(newProduct.getName());
        product.setPrice(newProduct.getPrice());
        product.setDescription(newProduct.getDescription());
        product.setImage(newProduct.getImage());
        product.setCategory(newProduct.getCategory());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchByName(String keyword) {
        return productRepository.findByNameContaining(keyword);
    }

    public Page<Product> searchByNamePage(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> result = productRepository.findByNameContaining(keyword, pageable);
        logger.info("📦 Tìm kiếm '{}': {} sản phẩm", keyword, result.getTotalElements());
        return result;
    }

    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    public Page<Product> getProductsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> result = productRepository.findAll(pageable);
        logger.info("📦 Phân trang tất cả: {} sản phẩm", result.getTotalElements());
        return result;
    }

    public Page<Product> getProductsByCategoryPage(Long categoryId, int page, int size) {
        logger.info("🔍 Tìm sản phẩm theo categoryId={}", categoryId);
        Category category = new Category();
        category.setId(categoryId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> result = productRepository.findByCategory(category, pageable);
        logger.info("📦 Tìm thấy {} sản phẩm cho categoryId={}", result.getTotalElements(), categoryId);
        return result;
    }
}