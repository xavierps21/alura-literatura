package com.alura.literatura;

import org.hibernate.Hibernate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;
import java.util.stream.Collectors;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(LiteraturaApplication.class, args);
    }

    private final RegisteredBookRepository registeredBookRepository;
    private final Scanner scanner;

    public LiteraturaApplication(RegisteredBookRepository registeredBookRepository) {
        this.registeredBookRepository = registeredBookRepository;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) throws Exception {
        boolean repeat = true;

        while (repeat) {
            System.out.println("Elija la opción a traves de su número");
            System.out.println("1- buscar libro por título");
            System.out.println("2- listar libros registrados");
            System.out.println("3- listar autores registrados");
            System.out.println("4- listar autores vivos en un determinado año");
            System.out.println("5- listar libros por idioma");
            System.out.println("0- salir");

            int option = readInt("Selecciona una opción:");

            switch (option) {
                case 1:
                    searchBookByTitle();
                    break;
                case 2:
                    listRegisteredBooks();
                    break;
                case 3:
                    listRegisteredAuthors();
                    break;
                case 4:
                    listLivingAuthors();
                    break;
                case 5:
                    listBooksByLanguage();
                    break;
                case 0:
                    System.out.println("Se ha finalizado el programa");
                    repeat = false;
                    break;
                default:
                    System.out.println("Opcion inválida");
                    break;
            }
        }
    }

    public int readInt(String message) {
        int value = 0;
        boolean valid = false;

        while (!valid) {
            System.out.print(message);

            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                valid = true;
            } else {
                System.out.println("El valor introducido no es válido");
                scanner.next();
            }
        }

        return value;
    }

    public void searchBookByTitle() throws InterruptedException {
        System.out.print("Ingrese el nombre del libro que desea buscar: ");
        String title = scanner.next();
        System.out.println("Buscando...");
        Iterable<GutendexBook> books = new GutendexRequestBuilder()
                .search(title)
                .sort("ascending")
                .execute();
        boolean hasBooks = false;

        for (GutendexBook book : books) {
            hasBooks = true;

            System.out.println("----- LIBRO -----");
            System.out.println("Título: " + book.getTitle());
            System.out.println("Autores: " + book.getAuthors().stream().map(GutendexPerson::getName).collect(Collectors.joining("; ")));
            System.out.println("Idiomas: " + String.join(", ", book.getLanguages()));
            System.out.println("Número de descargas: " + book.getDownloadCount());
            System.out.println("-----------------");
            System.out.println();

            if (!registeredBookRepository.existsById(book.getId())) {
                RegisteredBook registeredBook = new RegisteredBook();

                registeredBook.setId(book.getId());
                registeredBook.setTitle(book.getTitle());
                registeredBook.setAuthors(book.getAuthors().stream().map(author -> {
                    RegisteredBookAuthor registeredBookAuthor = new RegisteredBookAuthor();
                    registeredBookAuthor.setName(author.getName());
                    registeredBookAuthor.setBirthYear(author.getBirthYear());
                    registeredBookAuthor.setDeathYear(author.getDeathYear());
                    return registeredBookAuthor;
                }).collect(Collectors.toList()));
                registeredBook.setLanguages(book.getLanguages().stream().map(language -> {
                    RegisteredBookLanguage registeredBookLanguage = new RegisteredBookLanguage();
                    registeredBookLanguage.setLanguage(language);
                    return registeredBookLanguage;
                }).collect(Collectors.toList()));
                registeredBook.setDownloadCount(book.getDownloadCount());

                registeredBookRepository.save(registeredBook);
            }
        }

        if (!hasBooks) {
            System.out.println("No existen libros que coincidan con " + title);
        }
    }

    public void listRegisteredBooks() throws InterruptedException {
        System.out.println("Buscando...");
        Iterable<RegisteredBook> books = registeredBookRepository.findAll();
        boolean hasBooks = false;

        for (RegisteredBook book : books) {
            hasBooks = true;

            Hibernate.initialize(book.getLanguages());
            Hibernate.initialize(book.getAuthors());

            System.out.println("----- LIBRO -----");
            System.out.println("Título: " + book.getTitle());
            System.out.println("Autores: " + book.getAuthors().stream().map(RegisteredBookAuthor::getName).collect(Collectors.joining("; ")));
            System.out.println("Idiomas: " + book.getLanguages().stream().map(RegisteredBookLanguage::getLanguage).collect(Collectors.joining("; ")));
            System.out.println("Número de descargas: " + book.getDownloadCount());
            System.out.println("-----------------");
            System.out.println();
            Thread.sleep(250);
        }

        if (!hasBooks) {
            System.out.println("No hay libros registrados");
        }
    }

    public void listRegisteredAuthors() throws InterruptedException {
        System.out.println("Buscando...");
        Iterable<Object[]> authors = registeredBookRepository.getAllAuthors();
        boolean hasAuthors = false;

        for (Object[] author : authors) {
            hasAuthors = true;

            System.out.println("----- AUTOR -----");
            System.out.println("Nombre: " + author[0]);
            System.out.println("Fecha de nacimiento: " + author[1]);
            System.out.println("Fecha de muerte: " + author[2]);
            System.out.println("-----------------");
            System.out.println();
            Thread.sleep(250);
        }

        if (!hasAuthors) {
            System.out.println("No hay autores registrados");
        }
    }

    public void listLivingAuthors() throws InterruptedException {
        int year = readInt("Ingrese el año que desea buscar: ");

        System.out.println("Buscando...");
        Iterable<Object[]> authors = registeredBookRepository.getLivingAuthors(year);
        boolean hasAuthors = false;

        for (Object[] author : authors) {
            hasAuthors = true;

            System.out.println("----- AUTOR -----");
            System.out.println("Nombre: " + author[0]);
            System.out.println("Fecha de nacimiento: " + author[1]);
            System.out.println("Fecha de muerte: " + author[2]);
            System.out.println("-----------------");
            System.out.println();
            Thread.sleep(250);
        }

        if (!hasAuthors) {
            System.out.println("No hay autores registrados vivos para el año " + year);
        }
    }

    public void listBooksByLanguage() throws InterruptedException {
        System.out.println("Algunos de los idiomas disponibles son:");
        System.out.println("es - Español");
        System.out.println("en - Inglés");
        System.out.println("fr - Francés");
        System.out.println("all - Muestra todos los idiomas registrados");
        System.out.println();
        System.out.print("Ingrese el idioma que desea buscar: ");
        String language = scanner.next();
        Iterable<Object[]> countByLanguages = registeredBookRepository.countBooksByLanguage(language);
        boolean hasLanguages = false;

        for (Object[] countByLanguage : countByLanguages) {
            hasLanguages = true;

            System.out.println("----- IDIOMA -----");
            System.out.println("Idioma: " + countByLanguage[0]);
            System.out.println("Libros: " + countByLanguage[1]);
            System.out.println("-----------------");
            System.out.println();
            Thread.sleep(250);
        }

        if (!hasLanguages) {
            System.out.println("No hay libros registrados para el idioma " + language);
        }
    }
}
