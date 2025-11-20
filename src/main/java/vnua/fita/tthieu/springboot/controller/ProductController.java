// File: src/main/java/vnua/fita/tthieu/springboot/controller/ProductController.java
package vnua.fita.tthieu.springboot.controller;

import vnua.fita.tthieu.springboot.entity.Product;
import vnua.fita.tthieu.springboot.entity.Category;
import vnua.fita.tthieu.springboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    // Phân trang sản phẩm
    @GetMapping("/page")
    public Page<Product> getProductsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        logger.info("🔍 Gọi /page với page={}, size={}", page, size);
        Page<Product> result = productService.getProductsByPage(page, size);
        logger.info("📦 Kết quả tất cả: {} sản phẩm", result.getTotalElements());
        return result;
    }

    // Lấy danh sách tất cả sản phẩm
    @GetMapping
    public List<Product> getAllProducts() {
        logger.info("🔍 Gọi getAllProducts");
        List<Product> result = productService.getAllProducts();
        logger.info("📦 Kết quả tất cả: {} sản phẩm", result.size());
        return result;
    }
   
    // Tìm sản phẩm theo ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        logger.info("🔍 Gọi getProductById với id={}", id);
        return productService.getProductById(id);
    }

    // Tìm sản phẩm theo tên với phân trang
    @GetMapping("/search")
    public Page<Product> searchProduct(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        logger.info("🔍 Gọi search với keyword={}, page={}, size={}", keyword, page, size);
        Page<Product> result = productService.searchByNamePage(keyword, page, size);
        logger.info("📦 Kết quả tìm kiếm: {} sản phẩm", result.getTotalElements());
        return result;
    }

    // Lấy sản phẩm theo loại với phân trang
    @GetMapping("/category/{categoryId}")
    public Page<Product> getByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        logger.info("🔍 Gọi /category/{} với page={}, size={}", categoryId, page, size);
        Page<Product> result = productService.getProductsByCategoryPage(categoryId, page, size);
        logger.info("📦 Kết quả cho category {}: {} sản phẩm", categoryId, result.getTotalElements());
        return result;
    }

    // Thêm sản phẩm
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    // Cập nhật sản phẩm
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    // Xóa sản phẩm
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}