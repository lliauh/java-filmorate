package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Component
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    @Override
    public Film create(Film film) {
        film.validate();
        if (film.getId() != null) {
            findFilmById(film.getId());
        }

        String sql = "INSERT into films(name, description, release_date, duration) VALUES (?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setString(3, film.getReleaseDate().toString());
            ps.setString(4, film.getDuration().toString());

            return ps;
        }, keyHolder);

        if (film.getRate() != null) {
            String rateUpdateSql = "UPDATE films SET rate = ? WHERE film_id = ?;";
            jdbcTemplate.update(rateUpdateSql, film.getRate(), keyHolder.getKey().intValue());
        }

        if (film.getMpa() != null) {
            mpaStorage.validateMpa(film.getMpa().getId());

            String ratingUpdateSql = "UPDATE films SET rating_id = ? WHERE film_id = ?;";

            jdbcTemplate.update(ratingUpdateSql, film.getMpa().getId(), keyHolder.getKey().intValue());
        }

        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                genreStorage.validateGenre(genre.getId());

                String genreSql = "INSERT into film_genres(film_id, genre_id) VALUES (?, ?);";
                jdbcTemplate.update(genreSql, keyHolder.getKey().intValue(), genre.getId());
            }
        }

        return findFilmById(keyHolder.getKey().intValue());
    }

    @Override
    public Film update(Film film) {
        film.validate();
        findFilmById(film.getId());

        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ? WHERE film_id = ?;";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setString(3, film.getReleaseDate().toString());
            ps.setString(4, film.getDuration().toString());
            ps.setString(5, film.getId().toString());

            return ps;
        }, keyHolder);

        if (film.getRate() != null) {
            String rateUpdateSql = "UPDATE films SET rate = ? WHERE film_id = ?;";
            jdbcTemplate.update(rateUpdateSql, film.getRate(), keyHolder.getKey().intValue());
        }

        if (film.getMpa() != null) {
            mpaStorage.validateMpa(film.getMpa().getId());

            String ratingUpdateSql = "UPDATE films SET rating_id = ? WHERE film_id = ?;";

            jdbcTemplate.update(ratingUpdateSql, film.getMpa().getId(), keyHolder.getKey().intValue());
        }

        if (!film.getGenres().isEmpty()) {
            String clearGenresSql = "DELETE FROM film_genres WHERE film_id = ?;";
            jdbcTemplate.update(clearGenresSql, film.getId());

            for (Genre genre : film.getGenres()) {
                genreStorage.validateGenre(genre.getId());

                String genreSql = "INSERT into film_genres(film_id, genre_id) VALUES (?, ?);";
                jdbcTemplate.update(genreSql, film.getId(), genre.getId());
            }
        }

        return findFilmById(keyHolder.getKey().intValue());
    }

    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT * FROM films;";

        List<Film> allFilms = jdbcTemplate.query(sql, filmRowMapper);

        for (Film film : allFilms) {
            addGenresFromDb(film);
        }

        return allFilms;
    }

    @Override
    public Film findFilmById(Integer filmId) {

        String sql = "SELECT * FROM films WHERE film_id = ?;";

        Optional<Film> film = jdbcTemplate.query(sql, filmRowMapper, filmId)
                .stream()
                .findFirst();

        if (film.isPresent()) {
            Film result = film.get();
            addGenresFromDb(result);

            return result;
        } else {
            throw new NotFoundException(String.format("Фильм ID: %d не найден", filmId));
        }
    }

    private void addGenresFromDb(Film film) {
        String filmGenresSql = "SELECT * FROM film_genres WHERE film_id = ?;";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(filmGenresSql, film.getId());

        while (genreRows.next()) {
            film.addGenre(genreStorage.findGenreById(genreRows.getInt("genre_id")));
        }
    }

    @Override
    public Collection<Film> getTopRatedFilms(Integer size) {
        String sql = "SELECT * FROM films ORDER BY rate DESC LIMIT ?;";

        List<Film> topRatedFilms = jdbcTemplate.query(sql, filmRowMapper, size);

        for (Film film : topRatedFilms) {
            addGenresFromDb(film);
        }

        return topRatedFilms;
    }
}