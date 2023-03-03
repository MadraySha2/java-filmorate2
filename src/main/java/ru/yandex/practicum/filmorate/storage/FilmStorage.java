package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.DuplicateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getFilmsList();

    Film getFilmById(int id);

    Film addFilm(Film film) throws DuplicateException;

    Film updFilm(Film film);

    Film like(int id, int userId);

    Film unLike(int id, int userId);

    List<Film> getMostPopularFilms(int count);
}
