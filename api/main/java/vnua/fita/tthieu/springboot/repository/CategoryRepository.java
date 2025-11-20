package vnua.fita.tthieu.springboot.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vnua.fita.tthieu.springboot.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Tìm category theo tên
    Category findByName(String name);
}

