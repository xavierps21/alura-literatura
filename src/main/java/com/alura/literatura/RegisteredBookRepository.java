package com.alura.literatura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegisteredBookRepository extends JpaRepository<RegisteredBook, Integer> {
    @Query("SELECT l.language, COUNT(rb) AS count FROM RegisteredBook AS rb INNER JOIN rb.languages AS l WHERE (:language = 'all' OR l.language = :language) GROUP BY l.language")
    List<Object[]> countBooksByLanguage(@Param("language") String language);

    @Query("SELECT a.name, a.birthYear, a.deathYear FROM RegisteredBook AS rb INNER JOIN rb.authors AS a")
    List<Object[]> getAllAuthors();

    @Query("SELECT a.name, a.birthYear, a.deathYear FROM RegisteredBook AS rb INNER JOIN rb.authors AS a WHERE a.birthYear < :year AND a.deathYear > :year")
    List<Object[]> getLivingAuthors(@Param("year") int year);
}
