package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.storage.dao.FilmDB;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor
public class GenreController {
    private final FilmDB filmDB;
    @GetMapping
    public List<Genre> getGenres() {
        return filmDB.getGenres();
    }
    @GetMapping("/{id}")
    public Genre getGenreByID(@PathVariable Integer id) {
        return filmDB.getGenreById(id);
    }
}
