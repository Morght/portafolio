package com.morght.literalura.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public final class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private short birth;
    private short death;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "books_authors", joinColumns = @JoinColumn(name = "author_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> authorBooks = new HashSet<>();

    public Author(AuthorDTO authorDTO) {
        List<String> tempList = new ArrayList<>(Arrays.asList(authorDTO.name().split(",")));
        Collections.reverse(tempList);
        this.name = tempList.stream().collect(Collectors.joining(" "));
        this.birth = authorDTO.birth();
        this.death = authorDTO.death();
    }

    public void addBook(Book b) {
        authorBooks.add(b);
    }
}
