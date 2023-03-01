package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
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
    public Film getFilmById(int id) {
        return storage.getFilmById(id);
    }
    public Film addFilm(Film film) {
        return storage.addFilm(film);
    }

    public Film updFilm(Film film) {
        return storage.updFilm(film);
    }
    public Film likeFilm(int id, int userId) {
       return storage.like(id, userId);
    }
    public Film unlikeFilm(int id, int userId) {
        return storage.unLike(id, userId);
    }
    public List<Film> getMostPopularFilms(int count) {
        return storage.getMostPopularFilms(count);
    }
}
