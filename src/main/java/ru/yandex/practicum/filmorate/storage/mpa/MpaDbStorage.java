package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;
import ru.yandex.practicum.filmorate.model.mpa.MpaRowMapper;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public Mpa findMpaById(Integer id) {
        String sql = """
                SELECT *
                FROM ratings
                WHERE rating_id = ?;
                """;

        Optional<Mpa> rating = jdbcTemplate.query(sql, mpaRowMapper, id)
                .stream()
                .findFirst();

        if (rating.isPresent()) {
            return rating.get();
        } else {
            throw new NotFoundException(String.format("Рейтинг ID: %d не найден", id));
        }
    }

    @Override
    public Collection<Mpa> findAll() {
        String sql = """
                SELECT *
                FROM ratings;
                """;

        return jdbcTemplate.query(sql, mpaRowMapper);
    }

    @Override
    public void validateMpa(Integer id) {
        String sql = """
                SELECT *
                FROM ratings
                WHERE rating_id = ?;
                """;

        Optional<Mpa> rating = jdbcTemplate.query(sql, mpaRowMapper, id)
                .stream()
                .findFirst();

        if (!rating.isPresent()) {
            throw new ValidationException(String.format("Рейтинг ID: %d не найден", id));
        }
    }
}
