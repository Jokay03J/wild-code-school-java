package fr.jokay03j.myblog.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
import fr.jokay03j.myblog.model.Category;
import fr.jokay03j.myblog.repository.CategoryRepository;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getAll() {
        List<Category> categories = this.categoryRepository.findAll();
        if (categories.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(categories.stream().map(CategoryDTO::convert).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        Category createdCategory = this.categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        Category category = this.categoryRepository.findById(id).orElse(null);
        if (category == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(CategoryDTO.convert(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody Category categoryFormData) {
        Category category = this.categoryRepository.findById(id).orElse(null);
        if (category == null)
            return ResponseEntity.notFound().build();

        category.setName(categoryFormData.getName());
        category.setUpdatedAt(LocalDateTime.now());
        Category savedCategory = this.categoryRepository.save(category);

        return ResponseEntity.ok(CategoryDTO.convert(savedCategory));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Long> deleteCategory(@PathVariable Long id) {
        Category category = this.categoryRepository.findById(id).orElse(null);

        if (category == null)
            return ResponseEntity.notFound().build();
        this.categoryRepository.delete(category);
        return ResponseEntity.noContent().build();
    }
}
