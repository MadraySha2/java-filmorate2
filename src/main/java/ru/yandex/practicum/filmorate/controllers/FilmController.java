package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> filmsList = new HashMap<>();
    private int id = 0;

    @GetMapping
    public List<Film> getFilmsList() {

        if (filmsList.isEmpty()) {
            return null;
        }
        return new ArrayList<>(filmsList.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        id += 1;
        filmsList.put(id, film);
        film.setId(id);
        return film;
    }

    @PutMapping
    public Film updFilm(@Valid @RequestBody Film film) {
       if (filmsList.containsKey(film.getId())){
            filmsList.put(film.getId(), film);
        }
       else {
           throw new RuntimeException("User inst registered!");
       }
            return film;
    }

}
