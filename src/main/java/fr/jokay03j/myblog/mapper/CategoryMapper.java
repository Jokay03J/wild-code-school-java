package fr.jokay03j.myblog.mapper;

import java.util.stream.Collectors;

import fr.jokay03j.myblog.dto.CategoryDTO;
import fr.jokay03j.myblog.model.Category;

public class CategoryMapper {
  static public CategoryDTO convert(Category category) {
    CategoryDTO dto = new CategoryDTO();
    dto.setId(category.getId());
    dto.setName(category.getName());
    if (category.getArticles() != null)
      dto.setArticles(category.getArticles().stream().map(ArticleMapper::convert).collect(Collectors.toList()));
    dto.setCreatedAt(category.getCreatedAt());
    dto.setUpdatedAt(category.getUpdatedAt());
    return dto;

  }
}
