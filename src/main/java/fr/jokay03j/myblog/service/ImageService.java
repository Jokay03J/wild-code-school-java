package fr.jokay03j.myblog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.jokay03j.myblog.dto.ImageDTO;
import fr.jokay03j.myblog.exception.ResourceNotFoundException;
import fr.jokay03j.myblog.mapper.ImageMapper;
import fr.jokay03j.myblog.model.Image;
import fr.jokay03j.myblog.repository.ImageRepository;

@Service
public class ImageService {
  ImageRepository imageRepository;

  public ImageService(ImageRepository imageRepository) {
    this.imageRepository = imageRepository;
  }

  public List<ImageDTO> getAll() {
    List<Image> images = imageRepository.findAll();
    List<ImageDTO> imageDTOs = images.stream()
        .map(ImageMapper::convert)
        .collect(Collectors.toList());
    return imageDTOs;
  }

  public ImageDTO getOne(Long id) {
    Image image = imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found"));

    return ImageMapper.convert(image);
  }

  public ImageDTO create(Image image) {
    Image savedImage = imageRepository.save(image);
    return ImageMapper.convert(savedImage);
  }

  public ImageDTO update(Long id, Image imageDetails) {
    Image image = imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found"));

    image.setUrl(imageDetails.getUrl());
    Image savedImage = imageRepository.save(image);
    return ImageMapper.convert(savedImage);
  }

  public Long delete(Long id) {
    imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found"));
    imageRepository.deleteById(id);
    return id;
  }
}
