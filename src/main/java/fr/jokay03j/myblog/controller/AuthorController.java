package fr.jokay03j.myblog.controller;

import java.util.List;

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
import fr.jokay03j.myblog.service.AuthorService;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAll() {
        List<AuthorDTO> authors = this.authorService.getAll();
        if (authors == null || authors.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorDTO> getOne(@PathVariable Long id) {
        AuthorDTO author = this.authorService.getOne(id);
        if (author == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(author);
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> create(@RequestBody Author authorBody) {
        AuthorDTO savedAuthor = this.authorService.create(authorBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }

    @PutMapping("{id}")
    public ResponseEntity<AuthorDTO> update(@PathVariable Long id, @RequestBody Author authorBody) {
        AuthorDTO author = this.authorService.update(id, authorBody);
        if (author == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(author);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Long author = this.authorService.delete(id);
        if (author == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
