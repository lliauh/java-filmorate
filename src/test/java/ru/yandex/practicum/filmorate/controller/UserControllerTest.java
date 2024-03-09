package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

public class UserControllerTest {
    private final UserStorage inMemoryUserStorage = new InMemoryUserStorage();
    private final UserService userService = new UserService(inMemoryUserStorage);
    private final UserController userController = new UserController(inMemoryUserStorage, userService);
    private User user;

    @Test
    public void validUserTest() {
        user = createTestUser();
        boolean exceptionThrown = false;

        try {
            inMemoryUserStorage.validate(user);
        } catch (ValidationException e) {
            exceptionThrown = true;
        }

        Assertions.assertFalse(exceptionThrown);
    }

    @Test
    public void incorrectEmailValidationTest() {
        user = createTestUser();

        user.setEmail("user_test.ru");
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryUserStorage.validate(user);
        });
        Assertions.assertEquals("Email должен содержать @.", thrown.getMessage());

        user.setEmail("");
        thrown = Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryUserStorage.validate(user);
        });
        Assertions.assertEquals("Email не может быть пустым.", thrown.getMessage());
    }

    @Test
    public void incorrectLoginValidationTest() {
        user = createTestUser();

        user.setLogin("");
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryUserStorage.validate(user);
        });
        Assertions.assertEquals("Login не может быть пустым.", thrown.getMessage());

        user.setLogin("lo gin");
        thrown = Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryUserStorage.validate(user);
        });
        Assertions.assertEquals("Login не может содержать пробелы.", thrown.getMessage());
    }

    @Test
    public void incorrectBirthdayValidationTest() {
        user = createTestUser();

        user.setBirthday(LocalDate.of(3000, 12, 19));
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryUserStorage.validate(user);
        });
        Assertions.assertEquals("День рождения не может быть в будущем.", thrown.getMessage());
    }

    private User createTestUser() {
        User user = new User();

        user.setEmail("user@test.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1996,2,23));

        return user;
    }
}