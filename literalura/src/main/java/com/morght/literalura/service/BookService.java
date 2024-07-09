package com.morght.literalura.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.morght.literalura.models.Author;
import com.morght.literalura.models.AuthorDTO;
import com.morght.literalura.models.Book;
import com.morght.literalura.models.BookDTO;
import com.morght.literalura.models.Language;
import com.morght.literalura.repository.AuthorRepository;
import com.morght.literalura.repository.BookRepository;
import com.morght.literalura.repository.LangRepository;
import com.morght.literalura.utils.BookUtils;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepo;
    @Autowired
    private AuthorRepository authorRepo;
    @Autowired
    private LangRepository langRepository;
    public BookUtils utils = new BookUtils();

    public List<BookDTO> getAllBooks() {
        return utils.bookListToDtoList(bookRepo.findAll());
    }

    public List<AuthorDTO> getAllAuthors() {
        return utils.authorListToDtoList(authorRepo.findAll());
    }

    public List<Language> getAllLanguages() {
        return langRepository.findAll();
    }

    public List<BookDTO> listBooksByLang(String l) {
        Optional<Language> foundLang = langRepository.findByName(l);

        if (foundLang.isPresent()) {
            Language language = foundLang.get();
            return utils.bookListToDtoList(language.getBooks().stream().collect(Collectors.toList()));
        }
        return null;
    }

    public List<AuthorDTO> authorsByYear(short anio) {
        return utils.authorListToDtoList(authorRepo.findByBirthLessThanEqualAndDeathGreaterThanEqual(anio, anio));
    }

    public BookDTO findBookByTitle(String title) {
        Optional<Book> b = bookRepo.findByTitle(title);
        if (b.isPresent())
            return utils.bookToDto(b.get());

        return null;
    }

    public boolean save(BookDTO bookDto) {

        if (findBookByTitle(bookDto.title()) == null) {
            Book book = new Book(bookDto);
            bookRepo.save(book);

            // Check if language exist then add book and save in repo
            for (String l : bookDto.languages()) {
                Language language;
                Optional<Language> existLang = langRepository.findByName(l);
                if (existLang.isPresent()) {
                    language = existLang.get();

                } else {
                    language = new Language(l);
                }
                language.addBook(book);
                langRepository.save(language);
            }

            // Check if author exist then add book and save in repo
            for (AuthorDTO a : bookDto.authors()) {
                Author author;
                Optional<Author> authorInDb = authorRepo.findByName(a.name());
                if (authorInDb.isPresent()) {
                    author = authorInDb.get();

                } else {
                    author = new Author(a);
                }

                author.addBook(book);
                authorRepo.save(author);
            }

            return true;
        }
        return false;
    }

}
