package ru.practicum.shareit.booking;

import ru.practicum.shareit.exception.EnumStateException;

public enum State {
    ALL,
    PAST,
    CURRENT,
    FUTURE,
    WAITING,
    REJECTED;

    public static State validateStateAsString(String state) {
        try {
            return State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new EnumStateException("Unknown state: " + state);
        }
    }
}


