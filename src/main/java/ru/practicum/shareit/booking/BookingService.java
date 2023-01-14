package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {

    Booking findBookingById(long bookingId, long userId);

    Booking createBooking(BookingRequest bookingRequest, long userId);

    Booking changeApproved(long bookingId, long userId, boolean isApproved);

    List<BookingDTO> findAllForUser(long userId, String state);

    List<BookingDTO> findBookingsByOwner(Long userId, String state);

    List<Booking> checkBookingsForItem(long itemId, long userId);
}
