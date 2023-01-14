package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@UtilityClass
public class BookingMapper {

    public static BookingDTO toBookingDTO(Booking booking) {
        Item item = booking.getItem();
        User user = booking.getBooker();

        ItemForBooking itemForBooking = new ItemForBooking(item.getId(), item.getName());
        UserForBooking userForBooking = new UserForBooking(user.getId(), user.getName());

        return new BookingDTO(booking.getId(), booking.getStart(), booking.getEnd(), itemForBooking, userForBooking,
                booking.getStatus().toString());
    }
}
