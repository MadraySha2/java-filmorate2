package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDBTest {
    private final FilmDB filmStorage;

    @Test
    void getAllFilms() {
        filmStorage.addFilm(Film.builder()
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100L)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MPARating(3, "PG-13"))
                .build());
        filmStorage.addFilm(Film.builder()
                .name("test1")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100L)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MPARating(3, "PG-13"))
                .build());
        filmStorage.addFilm(Film.builder()
                .name("test2")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100L)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MPARating(3, "PG-13"))
                .build());

        List<Film> films = filmStorage.getFilmsList();
        assertThat(films.get(0).getName()).isEqualTo("test");
        assertThat(films.get(1).getDescription()).isEqualTo("test");
        assertThat(films.get(2).getId()).isEqualTo(3);
    }

    @Test
    void updateFilm() {
        filmStorage.addFilm(Film.builder()
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100L)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MPARating(3, "PG-13"))
                .build());
        Film film = filmStorage.getFilmById(1);
        film.setName("TestCheck");
        filmStorage.updateFilm(film);
        assertThat(filmStorage.getFilmById(1).getName()).isEqualTo("TestCheck");
    }

    @Test
    void getFilmById() {
        filmStorage.addFilm(Film.builder()
                        .id(4)
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100L)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MPARating(3, "PG-13"))
                .build());
        Film film = filmStorage.getFilmById(4);
        assertThat(film.getName()).isEqualTo("test");
    }

    @Test
    void testGetAllMpa() {
        List<MPARating> mpaRatingList = filmStorage.getMPAS();
        assertThat(mpaRatingList.size()).isEqualTo(5);
    }

    @Test
    public void testGetMpaById() {
        MPARating mpaRating = filmStorage.getMPAbyId(1);
        assertThat(mpaRating).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void testGetAllGenres() {
        List<Genre> genres = filmStorage.getGenres();
        assertThat(genres.size()).isEqualTo(6);
    }

    @Test
    public void testGetGenreById() {
        Genre genre = filmStorage.getGenreById(1);
        assertThat(genre).hasFieldOrPropertyWithValue("id", 1);
    }
}
