package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.like.Like;
import ru.yandex.practicum.filmorate.model.like.LikeRowMapper;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final LikeRowMapper likeRowMapper;
    private final FilmStorage filmStorage;

    @Override
    public void addLike(Integer filmId, Integer userId) {
        Optional<Like> like = checkIfExists(filmId, userId);

        if (like.isPresent()) {
            throw new AlreadyExistsException(String.format("Лайк фильма ID: %d от юзера ID: %d уже существует.", filmId,
                    userId));
        } else {
            String likeSql = "INSERT into likes(film_id, user_id) VALUES (?, ?);";
            jdbcTemplate.update(likeSql, filmId, userId);

            Film film = filmStorage.findFilmById(filmId);
            if (film.getRate() == null) {
                film.setRate(1);
            } else {
                film.setRate(film.getRate() + 1);
            }

            filmStorage.update(film);
        }
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        Optional<Like> like = checkIfExists(filmId, userId);

        if (like.isPresent()) {
            String deleteLikeSql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?;";
            jdbcTemplate.update(deleteLikeSql, filmId, userId);

            Film film = filmStorage.findFilmById(filmId);
            film.setRate(film.getRate() - 1);
            filmStorage.update(film);
        } else {
            throw new NotFoundException(String.format("Лайк фильма ID: %d от юзера ID: %d не найден.", filmId,
                    userId));
        }
    }

    private Optional<Like> checkIfExists(Integer filmId, Integer userId) {
        String sql = "SELECT * FROM likes WHERE film_id = ? AND user_id = ?;";

        Optional<Like> like = jdbcTemplate.query(sql, likeRowMapper, filmId, userId)
                .stream()
                .findFirst();

        return like;
    }
}
