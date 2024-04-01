package ru.yandex.practicum.filmorate.model.friendship;

import lombok.Data;

@Data
public class Friendship {
    private Integer firstUserId;
    private Integer secondUserId;

    public Friendship(Integer firstUserId, Integer secondUserId) {
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
    }
}
