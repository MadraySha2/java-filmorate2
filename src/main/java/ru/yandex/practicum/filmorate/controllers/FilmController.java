package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> filmsList = new HashMap<>();
    private int id = 0;

    @GetMapping("/films")
    public List<Film> getFilmsList() {

        if (filmsList.isEmpty()) {
            return null;
        }
        return filmsList.values().stream().toList();
    }

    @PostMapping(value = "/add-film")
    public Film addFilm(@Valid @RequestBody Film film) {
        id += 1;
        filmsList.put(id, film);
        film.setId(id);
        return film;
    }

    @PutMapping(value = "/upd-film")
    public Film updFilm(@Valid @RequestBody Film film) {
            filmsList.put(film.getId(), film);
            return film;
    }

}
