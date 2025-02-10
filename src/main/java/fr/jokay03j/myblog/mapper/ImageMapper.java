package fr.jokay03j.myblog.mapper;

import java.util.stream.Collectors;

import fr.jokay03j.myblog.dto.ImageDTO;
import fr.jokay03j.myblog.model.Article;
import fr.jokay03j.myblog.model.Image;

public class ImageMapper {
  public static ImageDTO convert(Image image) {
    ImageDTO imageDTO = new ImageDTO();
    imageDTO.setId(image.getId());
    imageDTO.setUrl(image.getUrl());
    imageDTO.setArticleIds(image.getArticles().stream().map(Article::getId).collect(Collectors.toList()));
    return imageDTO;
  }
}
