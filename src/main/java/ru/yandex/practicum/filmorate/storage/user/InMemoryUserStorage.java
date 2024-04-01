package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public User create(User user) {
        user.validate();

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (user.getId() == null) {
            user.setId(users.size() + 1);
        } else if (users.containsKey(user.getId())) {
            log.debug("Юзер уже существует: {}", user);
            throw new AlreadyExistsException("Юзер уже существует.");
        }

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        user.validate();

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            log.debug("Юзера не существует: {}", user);
            throw new NotFoundException("Юзера не существует.");
        }
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findUserById(Integer userId) {
        return users.values().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Юзер ID: %d не найден", userId)));
    }

    @Override
    public void deleteUserById(Integer userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
        }
    }
}
