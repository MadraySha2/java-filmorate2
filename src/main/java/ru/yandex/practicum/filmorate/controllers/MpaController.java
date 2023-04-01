package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.storage.dao.FilmDB;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class MpaController {
    private final FilmDB filmDB;
    @GetMapping
    public List<MPARating> getRatings() {
        return filmDB.getMPAS();
    }

    @GetMapping("/{id}")
    public MPARating getGenreByID(@PathVariable Integer id) {
        return filmDB.getMPAbyId(id);
    }
}
