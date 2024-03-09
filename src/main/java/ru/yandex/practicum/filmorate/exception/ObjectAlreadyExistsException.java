package ru.yandex.practicum.filmorate.exception;

public class ObjectAlreadyExistsException extends RuntimeException {
    public ObjectAlreadyExistsException(String s) {
        super(s);
    }
}
