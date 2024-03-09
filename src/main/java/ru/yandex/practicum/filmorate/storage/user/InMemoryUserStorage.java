package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public User create(User user) {
        validate(user);

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (user.getId() == null) {
            user.setId(users.size() + 1);
        } else if (users.containsKey(user.getId())) {
            log.debug("Юзер уже существует: {}", user);
            throw new ObjectAlreadyExistsException("Юзер уже существует.");
        }

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        validate(user);

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            log.debug("Юзера не существует: {}", user);
            throw new ObjectNotFoundException("Юзера не существует.");
        }
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public void validate(User user) {
        if (user.getEmail().isEmpty()) {
            throw new ValidationException("Email не может быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email должен содержать @.");
        }
        if (user.getLogin().isEmpty()) {
            throw new ValidationException("Login не может быть пустым.");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login не может содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("День рождения не может быть в будущем.");
        }
    }

    @Override
    public User findUserById(Integer userId) {
        return users.values().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Юзер ID: %d не найден", userId)));
    }
}
