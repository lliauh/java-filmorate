package ru.yandex.practicum.filmorate.model.like;

import lombok.Data;

@Data
public class Like {
    private Integer filmId;
    private Integer userId;

    public Like() {
    }

    public Like(Integer filmId, Integer userId) {
        this.filmId = filmId;
        this.userId = userId;
    }
}
