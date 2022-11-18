package ru.practicum.shareit.validate;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRequest;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.comment.CommentsDTO;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.EnumStateException;
import ru.practicum.shareit.exception.NotFoundIdException;
import ru.practicum.shareit.exception.WrongDateTimeException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Optional;


@Component
public class ValidaterForData {


    public User userIdIsPresent(Optional<User> user) {
        if (user.isEmpty()) throw new NotFoundIdException("Пользователь с указанным id не найден!");
        else {
            return user.get();
        }
    }

    public Item itemIdIsPresent(Optional<Item> item)  {
        if (item.isEmpty()) {
            throw new NotFoundIdException("Вещь с указанным id не найдена!");
        } else {
            return item.get();
        }
    }

    public Booking bookingIdIsPresent(Optional<Booking> booking) {

        if (booking.isEmpty()) {
            throw new NotFoundIdException("Книга с указанным id не найдена!");
        } else {
            return booking.get();
        }
    }

    public boolean bookingRequestIsTrue(BookingRequest bookingRequest) {
        if (bookingRequest.getStart().isAfter(bookingRequest.getEnd())) {
            throw new WrongDateTimeException("Время начала бронирования не может быть " +
                    "позже времени окончания бронирования!");
        }
        if (bookingRequest.getStart().equals(bookingRequest.getEnd())) {
            throw new WrongDateTimeException("Время начала бронирования не может быть " +
                    "равно времени окончания бронирования!");
        }
        if (bookingRequest.getStart().isBefore(LocalDateTime.now()) ||
                bookingRequest.getEnd().isBefore(LocalDateTime.now())) {
            throw new WrongDateTimeException("Время начала окончания бронирования не может быть раньше настоящего " +
                    "времени!");
        }
        return true;
    }

    public State validateStateAsString(String state)  {
        State stater;
        try {
            return stater = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new EnumStateException("Unknown state: " + state);
        }
    }

    public boolean isValidateComment(CommentsDTO commentsDTO) {
        if (commentsDTO.getText().isEmpty()) {
            throw new DataNotFoundException("Комментарий не должен быть пустым");
        } else {
            return true;
        }
    }


}
