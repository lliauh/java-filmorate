package ru.yandex.practicum.filmorate.model.user;

import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public User() {
    }

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void validate() {
        if (email.isEmpty()) {
            throw new ValidationException("Email не может быть пустым.");
        }
        if (!email.contains("@")) {
            throw new ValidationException("Email должен содержать @.");
        }
        if (login.isEmpty()) {
            throw new ValidationException("Login не может быть пустым.");
        }
        if (login.contains(" ")) {
            throw new ValidationException("Login не может содержать пробелы.");
        }
        if (birthday.isAfter(LocalDate.now())) {
            throw new ValidationException("День рождения не может быть в будущем.");
        }
        if (name == null) {
            setName(login);
        }
    }
}