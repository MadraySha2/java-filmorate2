package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.util.LikesComparator;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Comparator<Film> likesComparator = new LikesComparator();
    protected final Set<Film> filmsRating = new TreeSet<>(likesComparator);

    private final HashMap<Integer, Film> filmsList = new HashMap<>();
    private int id = 0;

    @Override
    public List<Film> getFilmsList() {
        return new ArrayList<>(filmsList.values());
    }

    @Override
    public Film getFilmById(int id) {
        if(filmsList.containsKey(id)) {
            return filmsList.get(id);
        }
        return null; // throw err
    }
    @Override
    public Film addFilm(Film film) {
        id += 1;
        filmsList.put(id, film);
        film.setId(id);
        return film;
    }

    @Override
    public Film updFilm(Film film) {
        if (filmsList.containsKey(film.getId())) {
            filmsList.put(film.getId(), film);
        } else {
            throw new RuntimeException("User inst registered!");
        }
        return film;
    }

    @Override
    public Film like(int id, int userId) {
        if (!filmsList.containsKey(id)) {
            return null;
        }
        Film film = filmsList.get(id);
        Set<Long> likes = film.getUserLikes();
        likes.add((long) userId);
        film.setUserLikes(likes);
        filmsList.put(id, film);
        return film;
    }

    @Override
    public Film unLike(int id, int userId) {
        if (!filmsList.containsKey(id)) {
            return null;
        }
        Film film = filmsList.get(id);
        Set<Long> likes = film.getUserLikes();
        likes.remove(userId);
        film.setUserLikes(likes);
        filmsList.put(id, film);
        return film;
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        filmsRating.addAll(filmsList.values());
        return filmsRating.stream().limit((long) count).collect(Collectors.toList());
    }

}
