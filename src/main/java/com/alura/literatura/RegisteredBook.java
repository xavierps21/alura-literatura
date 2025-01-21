package com.alura.literatura;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;

@Entity
public class RegisteredBook {
    @Id
    private Integer id;
    private String title;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "registered_book_id")
    private Collection<RegisteredBookAuthor> authors = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "registered_book_id")
    private Collection<RegisteredBookLanguage> languages;
    private Integer downloadCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection<RegisteredBookAuthor> getAuthors() {
        return authors;
    }

    public void setAuthors(Collection<RegisteredBookAuthor> authors) {
        this.authors = authors;
    }

    public Collection<RegisteredBookLanguage> getLanguages() {
        return languages;
    }

    public void setLanguages(Collection<RegisteredBookLanguage> languages) {
        this.languages = languages;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }
}
