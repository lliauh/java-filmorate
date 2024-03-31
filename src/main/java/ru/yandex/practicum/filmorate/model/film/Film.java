package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Integer rate;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    private Mpa mpa;

    public Film() {
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void validate() {
        if (name.isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (description.length() > 200) {
            throw new ValidationException("Описание фильма не может быть больше 200 символов.");
        }
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть ранее 28.12.1895.");
        }
        if (duration <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}