package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<String, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Создание юзера: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), user);

        if (isUserValid(user)) {
            user.setId(users.size() + 1);
            users.put(user.getLogin(), user);

            return user;
        } else {
            log.debug("Ошибка валидации юзера: {}", user);
            throw new ValidationException("Юзер не прошел валидацию.");
        }
    }

    @PutMapping
    public User update(@RequestBody User user, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Обновление юзера: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), user);

        if (isUserValid(user)) {
            if (users.containsKey(user.getLogin())) {
                user.setId(users.get(user.getLogin()).getId());
                users.put(user.getLogin(), user);
            } else {
                user.setId(users.size() + 1);
                users.put(user.getLogin(), user);
            }

            return user;
        } else {
            log.debug("Ошибка валидации юзера: {}", user);
            throw new ValidationException("Юзер не прошел валидацию.");
        }
    }

    public boolean isUserValid(User user) throws ValidationException {
        if (user.getEmail().isEmpty()) {
            throw new ValidationException("Email не может быть пустым.");
        } else if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email должен содержать @.");
        } else if (user.getLogin().isEmpty()) {
            throw new ValidationException("Login не может быть пустым.");
        } else if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login не может содержать пробелы.");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("День рождения не может быть в будущем.");
        } else {
            return true;
        }
    }
}