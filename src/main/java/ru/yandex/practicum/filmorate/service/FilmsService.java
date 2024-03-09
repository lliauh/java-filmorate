package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FilmsService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmsService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Integer filmId, Integer userId) {
        filmStorage.getFilms().get(filmId).getLikes().add(userId);
        filmStorage.getFilms().get(filmId).setLikesCount(filmStorage.getFilms().get(filmId).getLikes().size());
    }

    public void deleteLike(Integer filmId, Integer userId) {
        filmStorage.getFilms().get(filmId).getLikes().remove(userId);
        filmStorage.getFilms().get(filmId).setLikesCount(filmStorage.getFilms().get(filmId).getLikes().size());
    }

    public Collection<Film> getTopRatedFilms(Integer size) {
        return filmStorage.findAll().stream().sorted((f0, f1) -> {
            int comp = -1 * f0.getLikesCount().compareTo(f1.getLikesCount());
            return comp;
        }).limit(size).collect(Collectors.toList());
    }
}
