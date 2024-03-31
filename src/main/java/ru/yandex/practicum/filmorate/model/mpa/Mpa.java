package ru.yandex.practicum.filmorate.model.mpa;

import lombok.Data;

@Data
public class Mpa {
    private Integer id;
    private String name;

    public Mpa() {
    }

    public Mpa(Integer id) {
        this.id = id;
    }

    public Mpa(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}