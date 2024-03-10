package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addFriends(Integer firstUserId, Integer secondUserId) {
        userStorage.getUsers().get(firstUserId).getFriendsList().add(secondUserId);
        userStorage.getUsers().get(secondUserId).getFriendsList().add(firstUserId);
    }

    public void deleteFriends(Integer firstUserId, Integer secondUserId) {
        userStorage.getUsers().get(firstUserId).getFriendsList().remove(secondUserId);
        userStorage.getUsers().get(secondUserId).getFriendsList().remove(firstUserId);
    }

    public Collection<User> getMutualFriends(Integer firstUserId, Integer secondUserId) {
        Set<Integer> firstUserFriendsIds = userStorage.getUsers().get(firstUserId).getFriendsList();
        Set<Integer> secondUserFriendsIds = userStorage.getUsers().get(secondUserId).getFriendsList();
        Set<Integer> mutualFriendsIds = firstUserFriendsIds.stream()
                .filter(secondUserFriendsIds::contains)
                .collect(Collectors.toSet());

        List<User> mutualFriends = new ArrayList<>();

        for (User user : userStorage.findAll()) {
            if (mutualFriendsIds.contains(user.getId())) {
                mutualFriends.add(user);
            }
        }

        return mutualFriends;
    }

    public Collection<User> getFriendsById(Integer userId) {
        List<User> friends = new ArrayList<>();

        for (Integer friendId : userStorage.getUsers().get(userId).getFriendsList()) {
            friends.add(userStorage.findUserById(friendId));
        }

        return friends;
    }

    public Collection<User> getAllUsers() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User getUserById(Integer userId) {
        return userStorage.findUserById(userId);
    }
}
