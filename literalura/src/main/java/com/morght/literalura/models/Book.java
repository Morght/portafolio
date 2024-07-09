package com.morght.literalura.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long gutenbergId;
    private String title;
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "authorBooks", fetch = FetchType.EAGER)
    private Set<Author> authors = new HashSet<>();
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "books", fetch = FetchType.EAGER)
    private Set<Language> languages = new HashSet<>();
    private String copyright;
    private Long downloadCount;

    public Book(BookDTO bookData) {
        this.gutenbergId = bookData.gutenbergId();
        this.title = bookData.title();
        this.copyright = bookData.copyright();
        this.downloadCount = bookData.downloadCount();
    }

    public void addLang(Language l) {
        languages.add(l);
    }

    public void addAuthor(Author a) {
        authors.add(a);
    }

}
