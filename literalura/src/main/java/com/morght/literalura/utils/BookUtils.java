package com.morght.literalura.utils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.morght.literalura.models.Author;
import com.morght.literalura.models.AuthorDTO;
import com.morght.literalura.models.Book;
import com.morght.literalura.models.BookDTO;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookUtils {

    public BookDTO bookToDto(Book b) {
        return new BookDTO(b.getGutenbergId(), b.getTitle(),
                b.getAuthors().stream().map(a -> authorToDto(a)).collect(Collectors.toList()),
                b.getLanguages().stream().map(s -> s.getName()).collect(Collectors.toList()),
                b.getCopyright(), b.getDownloadCount());
    }

    public List<BookDTO> bookListToDtoList(List<Book> books) {
        return books.stream()
                .map(b -> bookToDto(b))
                .collect(Collectors.toList());
    }

    public AuthorDTO authorToDto(Author a) {
        return new AuthorDTO(a.getName(), a.getBirth(), a.getDeath());
    }

    public List<AuthorDTO> authorListToDtoList(List<Author> authors) {
        return authors.stream()
                .map(a -> authorToDto(a))
                .collect(Collectors.toList());
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public String bookData(BookDTO b) {
        return """

                ========== LIBRO ==========
                Titulo: """ + b.title() +
                "\nIdioma: " + b.languages().stream().collect(Collectors.joining(",")) +
                "\nAutor: " + b.authors().stream().map(AuthorDTO::name).collect(Collectors.joining()) +
                "\nNumero de descargas: " + b.downloadCount() +
                "\n";
    }

    public String authorData(AuthorDTO a) {
        return "========== AUTOR ==========\n" + a.name() + " (" + a.birth() + "-" + a.death() + ")";
    }
}
