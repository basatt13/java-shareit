package ru.practicum.shareit.exception;

public class EmailDoubleException extends RuntimeException {
    public EmailDoubleException(String message) {
        super(message);
    }
}
