package ru.yandex.practicum.filmorate.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {
    private final FilmService service;

    @GetMapping
    public List<Film> getFilmsList() {
        return service.getFilmsList();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return service.getFilmById(id);
    }

    @GetMapping("/popular?count={count}")
    public List<Film> getRatedFilmsList(@PathVariable Integer count) {
        return service.getMostPopularFilms(count);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return service.addFilm(film);
    }

    @PutMapping
    public Film updFilm(@Valid @RequestBody Film film) {
        return service.updFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        return service.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film unlikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        return service.unlikeFilm(id, userId);
    }
}
