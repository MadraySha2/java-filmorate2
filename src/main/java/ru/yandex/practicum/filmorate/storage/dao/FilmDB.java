package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDB implements FilmStorage {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getFilmsList() {
        String sqlQuery = "select * from FILM";
        return List.copyOf(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> convert(rs)));
    }

    @Override
    public Film getFilmById(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from film where id = ?", id);
        String getLikes = "select USER_ID from USER_LIKES where FILM_ID = ?";
        if (filmRows.next()) {
            Film film = Film.builder().id(filmRows.getInt("id"))
                    .name(filmRows.getString("name"))
                    .description(filmRows.getString("description"))
                    .duration(filmRows.getLong("duration"))
                    .releaseDate(filmRows.getDate("releasedate").toLocalDate())
                    .genres(convert(filmRows.getString("genres")))
                    .mpa(new MPARating(filmRows.getInt("MPARating_id"),
                            getMPAbyId(filmRows.getInt("MPARating_id")).getName()))
                    .build();
            film.getUserLikes().addAll(jdbcTemplate.queryForList(getLikes, Integer.class, id));
            return film;
        } else {
            throw new NotFoundException("Film not found!");
        }
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into film(name, description, duration, releasedate, genres, mparating_id) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setInt(3, film.getDuration().intValue());
            statement.setDate(4, Date.valueOf(film.getReleaseDate()));
            statement.setString(5, convert(film.getGenres()));
            statement.setInt(6, film.getMpa().getId());
            return statement;
        }, kh);
        film.setId(kh.getKey().intValue());
        System.out.println(film.getId());

        if (!film.getUserLikes().isEmpty()) {
            for (int userId : film.getUserLikes())
                likeFilm(film.getId(), userId);
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        System.out.println(film.getGenres());
        Film film1 = getFilmById(film.getId()); // проверка на существование фильма
        String sqlQuery = "update FILM " +
                "set NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, GENRES = ?, MPARATING_ID = ?, COUNT_LIKES = ? " +
                "where ID = ?";
        String checkLikes = "select USER_ID from USER_LIKES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                convert(film.getGenres()),
                film.getMpa().getId(),
                film.getUserLikes().size(),
                film.getId()
        );
        if (!film.getUserLikes().isEmpty()) {
            for (int userId : film.getUserLikes()) {
                likeFilm(film.getId(), userId);
            }
        }
        if (!film.getUserUnlikeBuffer().isEmpty()) {
            for (int userId : film.getUserUnlikeBuffer()) {
                unlikeFilm(film.getId(), userId);
            }
        }
        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        String sqlGetPopular = "select * from FILM order by COUNT_LIKES desc limit ?";
        return List.copyOf(jdbcTemplate.query(sqlGetPopular, (rs, rowNum) -> convert(rs), count));
    }

    private Film likeFilm(Integer id, Integer userId) {
        Film film = getFilmById(id);
        String sqlCheckUser = "select * from USERS where ID = ?";
        if (!jdbcTemplate.queryForRowSet(sqlCheckUser, userId).next()) {
            throw new NotFoundException("User not found!");
        }
        String sqlCheckFilm = "select * from USER_LIKES where film_id = ? and user_id = ?";
        SqlRowSet checkFilm = jdbcTemplate.queryForRowSet(sqlCheckFilm, id, userId);
        if (!checkFilm.next()) {
            String sqlInsertLikes = "insert into USER_LIKES (film_id, user_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlInsertLikes, id, userId);
        }
        film.getUserLikes().add(id);
        return film;
    }

    private Film unlikeFilm(Integer id, Integer userId) {
        Film film = getFilmById(id);
        String deleteLikes = "delete from USER_LIKES where FILM_ID = ? and USER_ID = ? ";
        jdbcTemplate.update(deleteLikes, id, userId);
        film.getUserLikes().remove(id);
        return film;
    }

    public List<MPARating> getMPAS() {
        String sqlGetAll = "select ID, RATING_NAME from MPARATING";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlGetAll);
        List<MPARating> ratings = new ArrayList<>();
        while (rs.next()) {
            MPARating mpaRating = new MPARating(rs.getInt("ID"), rs.getString("RATING_NAME"));
            ratings.add(mpaRating);
        }
        return ratings;
    }

    public MPARating getMPAbyId(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from MPARATING where ID = ?", id);
        if (!filmRows.next()) {
            throw new NotFoundException("MPA not find!");
        }
        return new MPARating(filmRows.getInt(1), filmRows.getString(2));
    }

    public List<Genre> getGenres() {
        String sqlGetAll = "select ID, GENRE_NAME from GENRES";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlGetAll);
        List<Genre> genres = new ArrayList<>();
        while (rs.next()) {
            Genre genre = new Genre(rs.getInt("ID"), rs.getString("GENRE_NAME"));
            genres.add(genre);
        }
        return genres;
    }

    public Genre getGenreById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from GENRES where ID = ?", id);
        if (!filmRows.next()) {
            throw new NotFoundException("Genre not find!");
        }
        return new Genre(filmRows.getInt(1), filmRows.getString(2));
    }

    private Film convert(ResultSet rs) throws SQLException {
        String getLikes = "select USER_ID from USER_LIKES where FILM_ID = ?";
        Film film = Film.builder().id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getLong("duration"))
                .releaseDate(rs.getDate("releasedate").toLocalDate())
                .genres(convert(rs.getString("genres")))
                .mpa(new MPARating(rs.getInt("MPARating_id"),
                        getMPAbyId(rs.getInt("MPARating_id")).getName()))
                .build();
        film.getUserLikes().addAll(jdbcTemplate.queryForList(getLikes, Integer.class, film.getId()));
        return film;
    }

    private List<Genre> convert(String genreIds) {
        List<Genre> genres = new ArrayList<>();
        //Set<Genre> genreSet = new HashSet<>();
        if (genreIds.isEmpty()) {
            return genres;
        }
        String[] convertStringRaw = genreIds.split(", ");
        for (String gID : convertStringRaw) {
            int id = Integer.parseInt(gID);
            Genre genre = new Genre(id, this.getGenreById(id).getName());
            genres.add(genre);
        }
        return genres;
    }

    private static String convert(List<Genre> genres) {
        StringBuilder genresToString = new StringBuilder();
        for (Genre genre : genres) {
            if (!genresToString.toString().contains(String.valueOf(genre.getId()))){
                genresToString.append(genre.getId()).append(", ");
            }
        }

        return genresToString.toString();
    }
}
