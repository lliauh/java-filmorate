package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;
import java.util.Collection;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    Collection<Film> findAll();

    Collection<Film> getTopRatedFilms(Integer size);

    Film findFilmById(Integer filmId);

    void deleteFilmById(Integer filmId);
}
