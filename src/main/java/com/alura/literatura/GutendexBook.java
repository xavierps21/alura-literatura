package com.alura.literatura;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Collection;
import java.util.HashMap;

public class GutendexBook {
    private Integer id;
    private String title;
    private Collection<String> subjects;
    private Collection<GutendexPerson> authors;
    private Collection<GutendexPerson> translators;
    private Collection<String> bookshelves;
    private Collection<String> languages;
    private Boolean copyright;
    @JsonAlias("media_type")
    private String mediaType;
    private HashMap<String, String> formats;
    @JsonAlias("download_count")
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

    public Collection<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(Collection<String> subjects) {
        this.subjects = subjects;
    }

    public Collection<GutendexPerson> getAuthors() {
        return authors;
    }

    public void setAuthors(Collection<GutendexPerson> authors) {
        this.authors = authors;
    }

    public Collection<GutendexPerson> getTranslators() {
        return translators;
    }

    public void setTranslators(Collection<GutendexPerson> translators) {
        this.translators = translators;
    }

    public Collection<String> getBookshelves() {
        return bookshelves;
    }

    public void setBookshelves(Collection<String> bookshelves) {
        this.bookshelves = bookshelves;
    }

    public Collection<String> getLanguages() {
        return languages;
    }

    public void setLanguages(Collection<String> languages) {
        this.languages = languages;
    }

    public boolean getCopyright() {
        return copyright;
    }

    public void setCopyright(boolean copyright) {
        this.copyright = copyright;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public HashMap<String, String> getFormats() {
        return formats;
    }

    public void setFormats(HashMap<String, String> formats) {
        this.formats = formats;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }
}
