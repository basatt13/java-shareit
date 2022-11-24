package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemForBooking item;
    private UserForBooking booker;
    private String status;
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class ItemForBooking {
    private long id;
    private String name;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class UserForBooking {
    private long id;
    private String name;
}

