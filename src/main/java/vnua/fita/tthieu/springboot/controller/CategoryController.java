// File: src/main/java/vnua/fita/tthieu/springboot/controller/CategoryController.java
package vnua.fita.tthieu.springboot.controller;

import vnua.fita.tthieu.springboot.entity.Category;
import vnua.fita.tthieu.springboot.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        logger.info("🔍 Gọi getAllCategories");
        List<Category> categories = categoryService.getAllCategories();
        logger.info("📦 Trả về {} danh mục", categories.size());
        return categories;
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        logger.info("🔍 Gọi getCategoryById với id={}", id);
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public Category addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}