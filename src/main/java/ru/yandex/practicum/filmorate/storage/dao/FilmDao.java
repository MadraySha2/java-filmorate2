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
import ru.yandex.practicum.filmorate.util.LikesComparator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDao implements FilmStorage {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getFilmsList() throws SQLException {
        String sqlQuery = "select f.ID, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION," +
                "fg.GENRE_ID, g.GENRE_NAME, f.MPARATING_ID , m.RATING_NAME, ul.USER_ID " +
                "from FILM f " +
                "left join FILMS_GENRES fg on f.ID = fg.FILM_ID " +
                "left join MPARATING m on m.ID = f.MPARATING_ID " +
                "left join GENRES g on fg.GENRE_ID = g.ID " +
                "left join USER_LIKES ul on f.ID = ul.FILM_ID";
        jdbcTemplate.queryForRowSet(sqlQuery);
        return convert(jdbcTemplate.queryForRowSet(sqlQuery));
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
                    .genres(convert(id))
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
        String sqlQuery = "insert into film(name, description, duration, releasedate, mparating_id) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setInt(3, film.getDuration().intValue());
            statement.setDate(4, Date.valueOf(film.getReleaseDate()));
            statement.setInt(5, film.getMpa().getId());
            return statement;
        }, kh);
        film.setId(kh.getKey().intValue());
        System.out.println(film.getId());
        if (getGenres() != null) {
            convert(film.getId(), film.getGenres());
        }
        if (!film.getUserLikes().isEmpty()) {
            for (int userId : film.getUserLikes())
                addFilmLike(film.getId(), userId);
        }

        return getFilmById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        System.out.println(film.getGenres());
        Film film1 = getFilmById(film.getId()); // проверка на существование фильма
        String sqlQuery = "update FILM " +
                "set NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, MPARATING_ID = ? " +
                "where ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        convert(film.getId(), film.getGenres());

        if (!film.getUserLikes().isEmpty()) {
            for (int userId : film.getUserLikes()) {
                addFilmLike(film.getId(), userId);
            }
        }
        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getMostPopularFilms(int count) throws SQLException {
        LikesComparator likesComparator = new LikesComparator();
        Set<Film> films = new HashSet<>(getFilmsList());
        return films.stream().sorted(likesComparator).limit(count).collect(Collectors.toList());
    }

    public Film addFilmLike(Integer id, Integer userId) {
        Film film = getFilmById(id);
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

    public Film deleteFilmLike(Integer id, Integer userId) {
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
            throw new NotFoundException("Genre not found!");
        }
        return new Genre(filmRows.getInt(1), filmRows.getString(2));
    }

    private List<Film> convert(SqlRowSet rs) {
        Map<Integer, Film> filmMap = new HashMap<>();
        while (rs.next()) {
            if (!filmMap.containsKey(rs.getInt("id"))) {
                Film film = Film.builder().id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .duration(rs.getLong("duration"))
                        .releaseDate(rs.getDate("releasedate").toLocalDate())
                        .mpa(new MPARating(rs.getInt("MPARating_id"),
                                getMPAbyId(rs.getInt("MPARating_id")).getName()))
                        .genres(new ArrayList<>())
                        .build();
                if (rs.getInt("genre_id") != 0) {
                    film.getGenres().add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
                }
                if (rs.getInt("user_id") != 0) {
                    film.getUserLikes().add(rs.getInt("user_id"));
                }
                filmMap.put(film.getId(), film);
            } else {
                Film film = filmMap.get(rs.getInt("id"));
                film.getUserLikes().add(rs.getInt("user_id"));
                Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
                System.out.println(genre);
                if (!film.getGenres().contains(genre)) {
                    film.getGenres().add(genre);
                }
                filmMap.put(film.getId(), film);
            }

        }
        return List.copyOf(filmMap.values());
    }

    private List<Genre> convert(Integer filmId) {
        List<Genre> genres = new ArrayList<>();
        String sqlGetGenres = "select GENRE_ID from FILMS_GENRES where FILM_ID = ?";
        List<Integer> genresId = List.copyOf(jdbcTemplate.queryForList(sqlGetGenres, Integer.class, filmId));
        if (genresId.isEmpty()) {
            return genres;
        }
        for (Integer gID : genresId) {
            Genre genre = new Genre(gID, this.getGenreById(gID).getName());
            if (!genres.contains(genre)) {
                genres.add(genre);
            }
        }
        return genres;
    }

    private void convert(Integer filmId, List<Genre> genres) {
        String sqlInsertGenres = "insert into FILMS_GENRES(film_id, genre_id) " +
                "values (?, ?)";
        String sqlDropFilmGenres = "delete from FILMS_GENRES where FILM_ID = ?";
        jdbcTemplate.update(sqlDropFilmGenres, filmId);
        if (!genres.isEmpty()) {
            for (Genre genre : genres) {
                jdbcTemplate.update(sqlInsertGenres, filmId, genre.getId());
            }
        }
    }
}