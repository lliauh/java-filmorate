package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmsService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    public void addLike(Integer filmId, Integer userId) {
        likeStorage.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        likeStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getTopRatedFilms(Integer size) {
        return filmStorage.getTopRatedFilms(size);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film getFilmById(Integer filmId) {
        return filmStorage.findFilmById(filmId);
    }

    public void deleteFilmById(Integer filmId) {
        filmStorage.deleteFilmById(filmId);
    }
}
