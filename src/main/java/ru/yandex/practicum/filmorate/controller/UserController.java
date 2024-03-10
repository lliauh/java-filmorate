package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return userService.getUserStorage().findAll();
    }

    @PostMapping
    public User create(@RequestBody User user, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Создание юзера: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), user);

        return userService.getUserStorage().create(user);
    }

    @PutMapping
    public User update(@RequestBody User user, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Обновление юзера: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), user);

        return userService.getUserStorage().update(user);
    }

    @GetMapping("/{userId}")
    public User findUser(@PathVariable("userId") Integer userId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Получение юзера ID: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), userId);

        return userService.getUserStorage().findUserById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable("id") Integer firstUserId, @PathVariable("friendId") Integer secondUserId,
                           HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Добавление в друзья" +
                        " юзеров ID: '{}, {}'", request.getMethod(), request.getRequestURI(),
                request.getQueryString(), firstUserId, secondUserId);

        userService.addFriends(firstUserId, secondUserId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable("id") Integer firstUserId, @PathVariable("friendId") Integer secondUserId,
                           HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Удаление из друзей" +
                        " юзеров ID: '{}, {}'", request.getMethod(), request.getRequestURI(),
                request.getQueryString(), firstUserId, secondUserId);

        userService.deleteFriends(firstUserId, secondUserId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable("id") Integer userId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Получение друзей юзера ID: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), userId);

        return userService.getFriendsById(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable("id") Integer firstUserId,
                                             @PathVariable("otherId") Integer secondUserId,
                                             HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}', Получение списка общих друзей" +
                        " юзеров ID: '{}, {}'", request.getMethod(), request.getRequestURI(), request.getQueryString(),
                firstUserId, secondUserId);

        return userService.getMutualFriends(firstUserId, secondUserId);
    }
}