package ru.practicum.shareit.exception;

public class NotFoundIdException extends RuntimeException {
    public NotFoundIdException(String message) {
        super(message);
    }
}
