package fr.jokay03j.myblog.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.jokay03j.myblog.dto.CategoryDTO;
import fr.jokay03j.myblog.exception.ResourceNotFoundException;
import fr.jokay03j.myblog.mapper.CategoryMapper;
import fr.jokay03j.myblog.model.Category;
import fr.jokay03j.myblog.repository.CategoryRepository;

@Service
public class CategoryService {
  CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public Long deleteCategory(Long id) {
    this.categoryRepository.deleteById(id);
    return id;
  }

  public List<CategoryDTO> getAll() {
    List<Category> categories = this.categoryRepository.findAll();
    return categories.stream().map(CategoryMapper::convert).collect(Collectors.toList());
  }

  public CategoryDTO createCategory(Category category) {
    category.setCreatedAt(LocalDateTime.now());
    category.setUpdatedAt(LocalDateTime.now());
    Category createdCategory = this.categoryRepository.save(category);
    return CategoryMapper.convert(createdCategory);
  }

  public CategoryDTO getCategory(Long id) {
    Category category = this.categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

    return CategoryMapper.convert(category);
  }

  public CategoryDTO updateCategory(Long id, Category categoryFormData) {
    Category category = this.categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

    category.setName(categoryFormData.getName());
    category.setUpdatedAt(LocalDateTime.now());
    Category savedCategory = this.categoryRepository.save(category);
    return CategoryMapper.convert(savedCategory);
  }
}
