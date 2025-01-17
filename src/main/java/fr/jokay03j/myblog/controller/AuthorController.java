package fr.jokay03j.myblog.controller;

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

import fr.jokay03j.myblog.dto.AuthorDTO;
import fr.jokay03j.myblog.model.Author;
import fr.jokay03j.myblog.repository.AuthorRepository;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAll() {
        List<Author> authors = this.authorRepository.findAll();
        if (authors == null || authors.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(authors.stream().map(AuthorDTO::convert).collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorDTO> getOne(@PathVariable Long id) {
        Author author = this.authorRepository.findById(id).orElse(null);
        if (author == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(AuthorDTO.convert(author));
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> create(@RequestBody Author authorBody) {
        Author savedAuthor = this.authorRepository.save(authorBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthorDTO.convert(savedAuthor));
    }

    @PutMapping("{id}")
    public ResponseEntity<AuthorDTO> update(@PathVariable Long id, @RequestBody Author authorBody) {
        Author author = this.authorRepository.findById(id).orElse(null);
        if (author == null)
            return ResponseEntity.notFound().build();
        author.setFirstname(authorBody.getFirstname());
        author.setLastname(authorBody.getLastname());
        Author savedAuthor = this.authorRepository.save(author);
        return ResponseEntity.ok(AuthorDTO.convert(savedAuthor));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Author author = this.authorRepository.findById(id).orElse(null);
        if (author == null)
            return ResponseEntity.notFound().build();
        this.authorRepository.delete(author);
        return ResponseEntity.noContent().build();
    }
}
