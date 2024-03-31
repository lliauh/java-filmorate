package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.mpa.Mpa;

import java.util.Collection;

public interface MpaStorage {
    Mpa findMpaById(Integer id);
    Collection<Mpa> findAll();
    void validateMpa(Integer id);
}
