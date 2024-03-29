package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmsService {
    private final FilmStorage filmStorage;

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
}
