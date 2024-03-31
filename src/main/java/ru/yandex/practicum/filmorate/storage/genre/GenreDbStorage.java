package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.model.genre.GenreRowMapper;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    @Override
    public Genre findGenreById(Integer id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?;";

        Optional<Genre> genre = jdbcTemplate.query(sql, genreRowMapper, id)
                .stream()
                .findFirst();

        if (genre.isPresent()) {
            return genre.get();
        } else {
            throw new NotFoundException(String.format("Жанр ID: %d не найден", id));
        }
    }

    @Override
    public Collection<Genre> findAll() {
        String sql = "SELECT * FROM genres;";

        return jdbcTemplate.query(sql, genreRowMapper);
    }

    @Override
    public void validateGenre(Integer id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?;";

        Optional<Genre> genre = jdbcTemplate.query(sql, genreRowMapper, id)
                .stream()
                .findFirst();

        if (!genre.isPresent()) {
            throw new ValidationException(String.format("Жанр ID: %d не найден", id));
        }
    }
}
