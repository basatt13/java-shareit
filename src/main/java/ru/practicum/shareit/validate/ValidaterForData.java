package ru.practicum.shareit.validate;

import org.springframework.stereotype.Repository;
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


@Repository
public class ValidaterForData {


    public User userIdIsPresent(Optional<User> user) throws NotFoundIdException {
        if (user.isEmpty()) throw new NotFoundIdException("Пользователь с указанным id не найден!");
        else {
            return user.get();
        }
    }

    public Item itemIdIsPresent(Optional<Item> item) throws NotFoundIdException {
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

    public boolean bookingRequestIsTrue(BookingRequest bookingRequest) throws WrongDateTimeException {
        if (bookingRequest.getStart().isAfter(bookingRequest.getEnd())) {
            throw new WrongDateTimeException("Время начала бронирования не может быть " +
                    "позже времени окончания бронирования!");
        }
        if (bookingRequest.getStart().isBefore(LocalDateTime.now()) ||
                bookingRequest.getEnd().isBefore(LocalDateTime.now())) {
            throw new WrongDateTimeException("Время начала окончания бронирования не может быть раньше настоящего " +
                    "времени!");
        }
        return true;
    }

    public State validateStateAsString(String state) throws EnumStateException {
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
