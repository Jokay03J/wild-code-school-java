package fr.jokay03j.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jokay03j.myblog.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
