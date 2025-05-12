package fr.jokay03j.myblog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.jokay03j.myblog.dto.CategoryDTO;
import fr.jokay03j.myblog.exception.ResourceNotFoundException;
import fr.jokay03j.myblog.mapper.CategoryMapper;
import fr.jokay03j.myblog.model.Category;
import fr.jokay03j.myblog.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private CategoryMapper categoryMapper;

  @InjectMocks
  private CategoryService categoryService;

  @Test
  void testGetAllCategories() {
    // Arrange
    // On crée deux catégories fictives
    Category category1 = new Category();
    category1.setName("Category 1");

    Category category2 = new Category();
    category2.setName("Category 2");

    // On simule le comportement du repository : quand on appelle findAll(), il
    // renvoie ces deux catégories
    when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

    // On crée les DTO correspondants aux deux catégories (ce que le mapper est
    // censé produire)
    CategoryDTO dto1 = new CategoryDTO();
    dto1.setName("Category 1");

    CategoryDTO dto2 = new CategoryDTO();
    dto2.setName("Category 2");

    // Act
    List<CategoryDTO> categories = categoryService.getAll();

    // Assert
    assertThat(categories).hasSize(2);
    assertThat(categories.get(0).getName()).isEqualTo("Category 1");
    assertThat(categories.get(1).getName()).isEqualTo("Category 2");
  }

  @Test
  void testGetCategoryById_CategoryExists() {
    // Arrange
    Category category = new Category();
    category.setName("Category 1");

    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

    // Act
    CategoryDTO result = categoryService.getCategory(1L);

    // Assert
    assertThat(result.getName()).isEqualTo("Category 1");
  }

  @Test
  void testGetCategoryById_CategoryNotFound() {
    // Arrange
    when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> categoryService.getCategory(99L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("Category not found");
  }
}