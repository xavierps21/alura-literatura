package com.alura.literatura;

import jakarta.persistence.*;

@Entity
public class RegisteredBookLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "registered_book_id")
    private RegisteredBook book;
    private String language;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RegisteredBook getBook() {
        return book;
    }

    public void setBook(RegisteredBook book) {
        this.book = book;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
