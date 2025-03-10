package fr.jokay03j.myblog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.jokay03j.myblog.dto.CategoryDTO;
import fr.jokay03j.myblog.dto.Category.CreateCategoryDTO;
import fr.jokay03j.myblog.mapper.CategoryMapper;
import fr.jokay03j.myblog.model.Category;
import fr.jokay03j.myblog.service.CategoryService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getAll() {
        List<CategoryDTO> categories = this.categoryService.getAll();
        if (categories.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CreateCategoryDTO cateboryBody) {
        Category category = CategoryMapper.toEntity(cateboryBody);
        CategoryDTO createdCategory = this.categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        CategoryDTO category = this.categoryService.getCategory(id);
        if (category == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody Category categoryFormData) {
        CategoryDTO savedCategory = this.categoryService.updateCategory(id, categoryFormData);
        if (savedCategory == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(savedCategory);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Long> deleteCategory(@PathVariable Long id) {
        Long category = this.categoryService.deleteCategory(id);

        if (category == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
