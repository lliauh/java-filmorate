package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.genre.Genre;

import java.util.Collection;

public interface GenreStorage {
    Genre findGenreById(Integer id);

    Collection<Genre> findAll();

    void validateGenre(Integer id);
}
