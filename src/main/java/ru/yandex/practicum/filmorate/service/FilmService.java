package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DuplicateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public List<Film> getFilmsList() {
        return storage.getFilmsList();
    }

    public Film getFilmById(Integer id) {
        return storage.getFilmById(id);
    }

    public Film addFilm(Film film) throws DuplicateException {
        return storage.addFilm(film);
    }

    public Film updFilm(Film film) {
        return storage.updFilm(film);
    }

    public Film likeFilm(Integer id, Integer userId) {
        return storage.like(id, userId);
    }

    public Film unlikeFilm(Integer id, Integer userId) {
        return storage.unLike(id, userId);
    }

    public List<Film> getMostPopularFilms(Integer count) {
        return storage.getMostPopularFilms(count);
    }
}
