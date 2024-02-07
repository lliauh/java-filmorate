package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

public class FilmControllerTest {
    private FilmController filmController = new FilmController();
    private Film film;

    @Test
    public void validFilmTest() {
        film = createTestFilm();

        Assertions.assertTrue(filmController.isFilmValid(film));
    }

    @Test
    public void emptyNameValidationTest() {
        film = createTestFilm();
        film.setName("");

        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.isFilmValid(film);
        });
        Assertions.assertEquals("Название фильма не может быть пустым.", thrown.getMessage());
    }

    @Test
    public void longDescriptionValidationTest() {
        film = createTestFilm();
        film.setDescription("Рэндла Патрика Макмёрфи, патологического преступника и бунтаря, переводят из " +
                "колонии в психиатрическую клинику, чтобы установить, является он душевнобольным или нет. В клинике" +
                "он обнаруживает, что отделение контролирует хладнокровная, строгая и одержимая распорядком старшая" +
                "медсестра Милдред Рэтчед. Макмёрфи намерен не подчиняться абсурдным, на его взгляд, правилам и " +
                "одновременно повеселиться от души. Его бунтарская натура заражает других пациентов, но сестра " +
                "Рэтчед решительно настроена пресечь это.");

        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.isFilmValid(film);
        });
        Assertions.assertEquals("Описание фильма не может быть больше 200 символов.", thrown.getMessage());
    }

    @Test
    public void earlyReleaseDateValidationTest() {
        film = createTestFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.isFilmValid(film);
        });
        Assertions.assertEquals("Дата релиза не может быть ранее 28.12.1895.", thrown.getMessage());
    }

    @Test
    public void negativeDurationValidationTest() {
        film = createTestFilm();
        film.setDuration(Duration.ofMinutes(-93));

        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.isFilmValid(film);
        });
        Assertions.assertEquals("Продолжительность фильма должна быть положительной.", thrown.getMessage());
    }

    private Film createTestFilm() {
        Film film = new Film();

        film.setName("Валидный фильм");
        film.setDescription("Валидное описание");
        film.setReleaseDate(LocalDate.of(1996, 2, 23));
        film.setDuration(93);

        return film;
    }
}
