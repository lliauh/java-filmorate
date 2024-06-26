package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmsService;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmsService filmsService;

    @GetMapping
    public Collection<Film> findAll(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmsService.getAllFilms();
    }

    @PostMapping
    public Film create(@RequestBody Film film, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Создание фильма: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), film);

        return filmsService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Обновление фильма: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), film);

        return filmsService.update(film);
    }

    @GetMapping("/{filmId}")
    public Film getById(@PathVariable("filmId") Integer filmId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Получение фильма ID: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), filmId);

        return filmsService.getFilmById(filmId);
    }

    @DeleteMapping("/{filmId}")
    public void deleteById(@PathVariable("filmId") Integer filmId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Удаление фильма ID: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), filmId);

        filmsService.deleteFilmById(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId,
                        HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Лайк фильма ID: '{}' от " +
                        "юзера ID: '{}'", request.getMethod(), request.getRequestURI(), request.getQueryString(),
                filmId, userId);

        filmsService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId,
                        HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Удаление лайка фильма ID: '{}'" +
                        " от юзера ID: '{}'", request.getMethod(), request.getRequestURI(), request.getQueryString(),
                filmId, userId);

        filmsService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count,
            HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Получение самых популярных " +
                        "фильмов в количестве: '{}'", request.getMethod(), request.getRequestURI(),
                request.getQueryString(), count);

        return filmsService.getTopRatedFilms(count);
    }
}