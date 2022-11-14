package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.EnumStateException;
import ru.practicum.shareit.exception.NotFoundIdException;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestBody BookingRequest bookingRequest,
                                 @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return bookingService.createBooking(bookingRequest, userId);
    }


    @PatchMapping("/{bookingId}")
    public Booking changeApproved(@PathVariable long bookingId,
                                  @RequestHeader(value = "X-Sharer-User-Id") long userId,
                                  @RequestParam boolean approved) throws NotFoundIdException {

        return bookingService.changeApproved(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking findBookingId(@PathVariable long bookingId,
                                 @RequestHeader(value = "X-Sharer-User-Id") long userId) throws NotFoundIdException {
        return bookingService.findBookingById(bookingId, userId);
    }

    @GetMapping()
    public List<BookingDTO> findBookingsByUser(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                               @RequestParam(value = "state", required = false, defaultValue = "ALL")
                                               String state) {


        return bookingService.findAllForUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDTO> findBookingByOwner(@RequestParam(value = "state", required = false,
            defaultValue = "ALL") String state,
                                               @RequestHeader("X-Sharer-User-Id")
                                               long ownerId) {
        return bookingService.findBookingsByOwner(ownerId, state);
    }

    @ExceptionHandler(EnumStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(EnumStateException e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
