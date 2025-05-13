package fr.jokay03j.myblog.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import fr.jokay03j.myblog.dto.CategoryDTO;
import fr.jokay03j.myblog.exception.ResourceNotFoundException;
import fr.jokay03j.myblog.filter.JwtAuthenticationFilter;
import fr.jokay03j.myblog.service.CategoryService;
import fr.jokay03j.myblog.service.CustomUserDetailsService;
import fr.jokay03j.myblog.service.JwtService;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CategoryService categoryService;

  @MockitoBean
  private JwtService jwtService;

  @MockitoBean
  private CustomUserDetailsService customUserDetailsService;

  @MockitoBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void testGetAllCategories() throws Exception {
    // Arrange
    CategoryDTO category1 = new CategoryDTO();
    category1.setName("Category 1");

    CategoryDTO category2 = new CategoryDTO();
    category2.setName("Category 2");

    when(categoryService.getAll()).thenReturn(List.of(category1, category2));

    // Act & Assert
    mockMvc.perform(get("/categories"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("Category 1"))
        .andExpect(jsonPath("$[1].name").value("Category 2"));
  }

  @Test
  void testGetCategoryById_CategoryExists() throws Exception {
    // Arrange
    CategoryDTO category = new CategoryDTO();
    category.setName("Category 1");

    when(categoryService.getCategory(1L)).thenReturn(category);

    // Act & Assert
    mockMvc.perform(get("/categories/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Category 1"));
  }

  @Test
  void testGetCategoryById_CategoryNotFound() throws Exception {
    // Arrange
    when(categoryService.getCategory(99L)).thenThrow(new ResourceNotFoundException("Category not found"));

    // Act & Assert
    mockMvc.perform(get("/api/categories/99"))
        .andExpect(status().isNotFound());
  }
}