package fr.jokay03j.myblog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.jokay03j.myblog.dto.AuthorDTO;
import fr.jokay03j.myblog.exception.ResourceNotFoundException;
import fr.jokay03j.myblog.mapper.AuthorMapper;
import fr.jokay03j.myblog.model.Author;
import fr.jokay03j.myblog.repository.AuthorRepository;

@Service
public class AuthorService {
  AuthorRepository authorRepository;

  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  public List<AuthorDTO> getAll() {
    List<Author> authors = this.authorRepository.findAll();
    if (authors == null || authors.isEmpty())
      return null;
    return authors.stream().map(AuthorMapper::convert).collect(Collectors.toList());
  }

  public AuthorDTO getOne(Long id) {
    Author author = this.authorRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
    if (author == null)
      return null;
    return AuthorMapper.convert(author);
  }

  public AuthorDTO create(Author authorBody) {
    Author savedAuthor = this.authorRepository.save(authorBody);
    return AuthorMapper.convert(savedAuthor);
  }

  public AuthorDTO update(Long id, Author authorBody) {
    Author author = this.authorRepository.findById(id).orElse(null);
    if (author == null)
      return null;
    author.setFirstname(authorBody.getFirstname());
    author.setLastname(authorBody.getLastname());
    Author savedAuthor = this.authorRepository.save(author);
    return AuthorMapper.convert(savedAuthor);
  }

  public Long delete(Long id) {
    Author author = this.authorRepository.findById(id).orElse(null);
    if (author == null)
      return null;
    this.authorRepository.deleteById(id);
    return id;
  }
}
