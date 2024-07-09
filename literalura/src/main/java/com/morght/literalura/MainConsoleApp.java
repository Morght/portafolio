package com.morght.literalura;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.morght.literalura.models.AuthorDTO;
import com.morght.literalura.models.BookDTO;
import com.morght.literalura.models.Language;
import com.morght.literalura.models.SearchResult;
import com.morght.literalura.service.API;
import com.morght.literalura.service.BookService;
import com.morght.literalura.utils.BookUtils;
import com.morght.literalura.utils.JsonTransform;

public class MainConsoleApp {

    @Autowired
    private final BookService service;

    private final Scanner scanner = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/";
    private final String NO_VALID = "***Opcion no valida***";
    private final API api = new API();
    private final JsonTransform jsonTransform = new JsonTransform();
    private final BookUtils utils;

    public MainConsoleApp(BookService service) {
        this.service = service;
        this.utils = service.utils;
    }

    public void start() {

        boolean isFinished = false;

        while (!isFinished) {

            System.out.println("""
                    ================================================
                    1 - Buscar libro por titulo y/con/o autor
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado anio
                    5 - Listar libros por idioma
                    6 - Salir
                    ================================================
                    Ingresa el numero de la opcion deseada:
                    """);
            String userInput = scanner.nextLine();

            switch (userInput.trim()) {
                case "1" -> findBookByTitle();
                case "2" -> showRegisteredBooks();
                case "3" -> showRegisteredAuthors();
                case "4" -> showAuthorsByYear();
                case "5" -> showBooksByLanguage();
                case "6" -> isFinished = true;
                default -> System.out.println(NO_VALID);
            }
        }

    }

    private void showBooksByLanguage() {
        List<Language> languages = service.getAllLanguages();
        System.out.println("Elige el idioma:");
        byte idiomaSelected = 0;
        while (idiomaSelected != languages.size() + 1) {

            for (byte i = 0; i < languages.size(); i++) {
                System.out.println((i + 1) + " - " + languages.get(i).getName());
            }
            System.out.println((languages.size() + 1) + " - Regresar al menu***");
            String userSelection = scanner.nextLine();

            try {
                idiomaSelected = Byte.parseByte(userSelection);
                if (idiomaSelected > 0 && idiomaSelected < languages.size() + 1) {
                    service.listBooksByLang(languages.get(idiomaSelected - 1).getName()).stream()
                            .forEach(b -> System.out.println(utils.bookData(b)));
                    idiomaSelected = (byte) (languages.size() + 1);
                } else {
                    System.out.println(NO_VALID);
                }

            } catch (NumberFormatException e) {
                System.out.println(NO_VALID);
            }
        }
    }

    private void showAuthorsByYear() {
        System.out.println("Ingresa el anio que desees buscar: ");
        String userInput = scanner.nextLine();
        try {
            short anio = Short.parseShort(userInput);
            List<AuthorDTO> results = service.authorsByYear(anio);
            if (results.isEmpty()) {
                System.out.println("No hay resultados");
            } else {
                for (AuthorDTO autor : results) {
                    System.out.println(utils.authorData(autor));
                }
            }
        } catch (NumberFormatException e) {
            e.getMessage();
        }

    }

    private void showRegisteredAuthors() {
        service.getAllAuthors().stream().forEach(a -> System.out.println(utils.authorData(a)));
    }

    private void showRegisteredBooks() {
        service.getAllBooks().stream().forEach(b -> System.out.println(utils.bookData(b)));
    }

    private void findBookByTitle() {
        System.out.println("Escribe el nombre y/con/o autor del libro a buscar:");
        String bookToFind = scanner.nextLine();

        if (bookToFind.matches("^[\\p{L} .'-]+$")) {
            String url = URL_BASE + "?search=" + bookToFind.replace(" ", "%20");
            String apiResult = api.getData(url);
            SearchResult searchResult = jsonTransform.toClass(apiResult, SearchResult.class);

            if (searchResult.count() > 0) {
                List<BookDTO> bookList = searchResult.results()
                        .stream()
                        .filter(BookUtils.distinctByKey(BookDTO::title))
                        .collect(Collectors.toList());

                if (bookList.size() == 1) {

                    if (service.save(bookList.get(0))) {
                        System.out.println("Libro registrado: \n"
                                + utils.bookData(service.findBookByTitle(bookList.get(0).title())));
                    } else {
                        System.out.println("El libro ya se encuentra en el registro");
                    }

                } else if (bookList.size() > 1) {
                    boolean backToMenu = false;

                    while (!backToMenu) {
                        System.out.println("Se encontraron estos libros:");

                        for (int i = 0; i < bookList.size(); i++) {
                            System.out.println((i + 1) + " - \"" + bookList.get(i).title() + "\" de \""
                                    + bookList.get(i).authors().stream()
                                            .map(a -> a.name().replace(",", ""))
                                            .collect(Collectors.joining(","))
                                    + "\"");
                        }
                        System.out.println(bookList.size() + 1 + " - Regresar al menu");
                        System.out.println("Ingresa el numero del libro a guardar :");
                        String userSelection = scanner.nextLine();

                        try {
                            short bookSelected = Short.parseShort(userSelection);

                            if (bookSelected > 0 && bookSelected <= bookList.size()) {

                                if (service.save(bookList.get(bookSelected - 1))) {
                                    System.out.println(
                                            "\nLibro agregado: \n" + utils.bookData(
                                                    service.findBookByTitle(bookList.get(bookSelected - 1).title())));
                                } else {
                                    System.out.println("El libro ya estaba guardado");
                                }
                                backToMenu = true;
                            } else if (bookSelected == bookList.size() + 1) {
                                backToMenu = true;
                            } else {
                                System.out.println(NO_VALID);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(NO_VALID);
                        }

                    }
                }

            } else {
                System.out.println("No se encontraron resultados");
            }
        } else {
            System.out.println("****Solo se aceptan letras****");
        }
    }
}
