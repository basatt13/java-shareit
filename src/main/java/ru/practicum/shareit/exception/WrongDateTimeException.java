package ru.practicum.shareit.exception;

public class WrongDateTimeException extends RuntimeException {
    public WrongDateTimeException(String message) {
        super(message);
    }
}
