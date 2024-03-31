package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Qualifier("inMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        validate(film);

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
        validate(film);

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

    private void validate(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма не может быть больше 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть ранее 28.12.1895.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }

    @Override
    public Film findFilmById(Integer filmId) {
        return films.values().stream()
                .filter(f -> f.getId().equals(filmId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Фильм ID: %d не найден", filmId)));
    }

    @Override
    public Collection<Film> getTopRatedFilms(Integer size) {
        return findAll().stream().sorted((f0, f1) -> {
            int comp = -1 * f0.getRate().compareTo(f1.getRate());
            return comp;
            }).limit(size).collect(Collectors.toList());
    }
}
