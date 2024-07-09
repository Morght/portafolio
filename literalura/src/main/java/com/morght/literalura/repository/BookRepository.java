package com.morght.literalura.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.morght.literalura.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByGutenbergId(Long gutenbergId);

    Optional<Book> findByTitle(String title);

}
