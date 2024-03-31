package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import ru.yandex.practicum.filmorate.model.friendship.Friendship;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    public void addFriends(Integer firstUserId, Integer secondUserId) {
        userStorage.findUserById(firstUserId);
        userStorage.findUserById(secondUserId);
        friendshipStorage.addFriends(firstUserId, secondUserId);
    }

    public void deleteFriends(Integer firstUserId, Integer secondUserId) {
        userStorage.findUserById(firstUserId);
        userStorage.findUserById(secondUserId);
        friendshipStorage.deleteFriends(firstUserId, secondUserId);
    }

    public Collection<User> getMutualFriends(Integer firstUserId, Integer secondUserId) {
        userStorage.findUserById(firstUserId);
        userStorage.findUserById(secondUserId);

        List<Friendship> firstUserFriends = friendshipStorage.findFriendsByUserId(firstUserId);
        List<Friendship> secondUserFriends = friendshipStorage.findFriendsByUserId(secondUserId);

        Set<Integer> firstUserFriendsIds = new HashSet<>();
        for (Friendship friendship : firstUserFriends) {
            firstUserFriendsIds.add(friendship.getSecondUserId());
        }

        Set<Integer> secondUserFriendsIds = new HashSet<>();
        for (Friendship friendship : secondUserFriends) {
            secondUserFriendsIds.add(friendship.getSecondUserId());
        }

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
        userStorage.findUserById(userId);
        List<Friendship> userFriendships = friendshipStorage.findFriendsByUserId(userId);

        Set<Integer> friendsIds = new HashSet<>();
        for (Friendship friendship : userFriendships) {
            friendsIds.add(friendship.getSecondUserId());
        }

        List<User> friends = new ArrayList<>();
        for (Integer friendId : friendsIds) {
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
