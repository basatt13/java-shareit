package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.NotFoundIdException;
import ru.practicum.shareit.exception.WrongDateTimeException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.validate.ValidaterForData;


import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor

public class BookingServiceImpl implements BookingService {

    private final ValidaterForData validaterForData;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Booking createBooking(BookingRequest bookingRequest, long userId) throws NoSuchElementException,
            DataNotFoundException, WrongDateTimeException {
        Booking booking = new Booking();
        if (validaterForData.bookingRequestIsTrue(bookingRequest)) {
            User user = validaterForData.userIdIsPresent(userRepository.findById(userId));
            Item item = validaterForData.itemIdIsPresent(itemRepository.findById(bookingRequest.getItemId()));
            booking.setStatus(Status.WAITING);
            booking.setItem(item);
            booking.setBooker(user);
            booking.setStart(bookingRequest.getStart());
            booking.setEnd(bookingRequest.getEnd());
            if (item.getOwner().getId() == userId) {
                throw new NotFoundIdException("Владелец вещи = автору бронирования");
            }
            if (!item.isAvailable()) {
                throw new DataNotFoundException("Вещью кто-то пользуется");
            }
        }
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking changeApproved(long bookingId, long userId, boolean isApproved) {
        Booking booking = validaterForData.bookingIdIsPresent(bookingRepository.findById(bookingId));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundIdException("Статус бронирования может менять только хозяин вещи");
        } else if (booking.getStatus() == Status.APPROVED) {
            throw new DataNotFoundException("Статус бронирования уже установлен");
        } else {
            booking.setStatus(isApproved ? Status.APPROVED : Status.REJECTED);
        }
        bookingRepository.save(booking);
        return booking;
    }

    @Override
    public Booking findBookingById(long bookingId, long userId) {
        Booking booking = validaterForData.bookingIdIsPresent(bookingRepository.findById(bookingId));
        if (booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId) {
            return booking;
        } else {
            throw new NotFoundIdException("Получение данных о конкретном бронировании может быть выполнено либо автором" +
                    " бронирования, либо владельцем вещи, к которой относится бронирование");
        }
    }

    @Override
    public List<BookingDTO> findAllForUser(long userId, String state1) throws NoSuchElementException {
        State state = validaterForData.validateStateAsString(state1);
        User user = validaterForData.userIdIsPresent(userRepository.findById(userId));
        switch (state) {
            case PAST:
                return bookingRepository.findAllByBookerAndEndIsBeforeOrderByStartDesc(user, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDTO)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(user, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDTO)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllByBookerAndStatusIsOrderByStartDesc(user, Status.WAITING).stream()
                        .map(BookingMapper::toBookingDTO)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findAllByBookerAndStatusIsOrderByStartDesc(user, Status.REJECTED).stream()
                        .map(BookingMapper::toBookingDTO)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.getByCurrentStatus(userId).stream()
                        .map(BookingMapper::toBookingDTO)
                        .collect(Collectors.toList());

            default:
                return bookingRepository.findAllByBookerOrderByStartDesc(user).stream()
                        .map(BookingMapper::toBookingDTO)
                        .collect(Collectors.toList());
        }
    }

    @Override
    public List<BookingDTO> findBookingsByOwner(Long userId, String state1) {
        User owner = validaterForData.userIdIsPresent(userRepository.findById(userId));

        State state = validaterForData.validateStateAsString(state1);

        List<Booking> bookings = bookingRepository.findBookingByOwner(owner.getId());

        if (bookings.isEmpty()) {
            throw new NoSuchElementException("У пользователя с таким ID нет ни одной брони.");
        }

        switch (state) {
            case ALL:
                break;
            case PAST:
                bookings = bookingRepository.findForOwnerPast(owner.getId());
                break;
            case FUTURE:
                bookings = bookingRepository.findForOwnerFuture(owner.getId());
                break;
            case WAITING:
                bookings = bookingRepository.findForOwnerByStatus(owner.getId(), Status.WAITING.toString());
                break;
            case REJECTED:
                bookings = bookingRepository.findForOwnerByStatus(owner.getId(), Status.REJECTED.toString());
                break;
            case CURRENT:
                bookings = bookingRepository.findForOwnerStartEnd(owner.getId());
                break;
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> checkBookingsForItem(long itemId, long userId) {
        User booker = validaterForData.userIdIsPresent(userRepository.findById(userId));
        return bookingRepository.findAllByBookerAndItemIdAndStatus(booker, itemId, Status.APPROVED);
    }
}
