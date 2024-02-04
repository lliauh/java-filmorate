package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<String, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Создание фильма: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), film);

        if (isFilmValid(film)) {
            film.setId(films.size() + 1);
            films.put(film.getName(), film);

            return film;
        } else {
            log.debug("Ошибка валидации фильма: {}", film);
            throw new ValidationException("Фильм не прошел валидацию.");
        }
    }

    @PutMapping
    public Film update(@RequestBody Film film, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Обновление фильма: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), film);

        if (isFilmValid(film)) {
            if (films.containsKey(film.getName())) {
                film.setId(films.get(film.getName()).getId());
                films.put(film.getName(), film);
            } else {
                film.setId(films.size() + 1);
                films.put(film.getName(), film);
            }

            return film;
        } else {
            log.debug("Ошибка валидации фильма: {}", film);
            throw new ValidationException("Фильм не прошел валидацию.");
        }
    }

    public boolean isFilmValid(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма не может быть больше 200 символов.");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть ранее 28.12.1895.");
        } else if (film.getDuration().getSeconds() < 1) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        } else {
            return true;
        }
    }
}