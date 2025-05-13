package fr.jokay03j.myblog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import fr.jokay03j.myblog.model.Category;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryRepositoryTest {

  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  void testFindAllCategories() {
    // Arrange : créer et sauvegarder des catégories dans la base de données
    LocalDateTime now = LocalDateTime.now();
    Category category1 = new Category();
    category1.setName("Category 1");
    category1.setCreatedAt(now);
    category1.setUpdatedAt(now);

    Category category2 = new Category();
    category2.setName("Category 2");
    category2.setCreatedAt(now);
    category2.setUpdatedAt(now);

    categoryRepository.saveAll(List.of(category1, category2));

    // Act : récupérer toutes les catégories via findAll()
    List<Category> categories = categoryRepository.findAll();

    // Assert : vérifier que la liste retournée contient bien les catégories
    assertThat(categories).hasSize(2);
    assertThat(categories.get(0).getName()).isEqualTo("Category 1");
    assertThat(categories.get(1).getName()).isEqualTo("Category 2");
  }
}