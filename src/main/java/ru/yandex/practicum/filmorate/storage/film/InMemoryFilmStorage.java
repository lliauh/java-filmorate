package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        film.validate();

        if (film.getId() == null) {
            film.setId(films.size() + 1);
        } else if (films.containsKey(film.getId())) {
            log.debug("Фильм уже существует: {}", film);
            throw new AlreadyExistsException("Фильм уже существует.");
        }

        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        film.validate();

        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            log.debug("Фильма не существует: {}", film);
            throw new NotFoundException("Фильма не существует.");
        }
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findFilmById(Integer filmId) {
        return films.values().stream()
                .filter(f -> f.getId().equals(filmId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Фильм ID: %d не найден", filmId)));
    }

    @Override
    public void deleteFilmById(Integer filmId) {
        if (films.containsKey(filmId)) {
            films.remove(filmId);
        }
    }

    @Override
    public Collection<Film> getTopRatedFilms(Integer size) {
        return findAll().stream().sorted((f0, f1) -> {
            int comp = -1 * f0.getRate().compareTo(f1.getRate());
            return comp;
            }).limit(size).collect(Collectors.toList());
    }
}
