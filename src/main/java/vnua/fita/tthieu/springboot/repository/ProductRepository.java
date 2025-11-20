// File: src/main/java/vnua/fita/tthieu/springboot/repository/ProductRepository.java
package vnua.fita.tthieu.springboot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vnua.fita.tthieu.springboot.entity.Category;
import vnua.fita.tthieu.springboot.entity.Product;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Tìm sản phẩm theo tên (tìm gần đúng)
    List<Product> findByNameContaining(String keyword);
    
    Page<Product> findByNameContaining(String keyword, Pageable pageable);
    
    // Tìm theo loại sản phẩm
    List<Product> findByCategory(Category category);
    
    // Thêm method hỗ trợ phân trang theo category
    Page<Product> findByCategory(Category category, Pageable pageable);
}