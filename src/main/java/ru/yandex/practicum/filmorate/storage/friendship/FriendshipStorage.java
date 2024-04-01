package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.friendship.Friendship;

import java.util.List;

public interface FriendshipStorage {
    void addFriends(Integer firstUserId, Integer secondUserId);

    void deleteFriends(Integer firstUserId, Integer secondUserId);

    List<Friendship> findFriendsByUserId(Integer userId);
}
