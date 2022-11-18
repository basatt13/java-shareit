package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.EnumStateException;
import ru.practicum.shareit.exception.NotFoundIdException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDTO create(@Valid @RequestBody BookingRequest bookingRequest,
                                 @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return BookingMapper.toBookingDTO(bookingService.createBooking(bookingRequest, userId));
    }


    @PatchMapping("/{bookingId}")
    public BookingDTO changeApproved(@Valid @PathVariable long bookingId,
                                  @RequestHeader(value = "X-Sharer-User-Id") long userId,
                                  @RequestParam boolean approved) {

        return BookingMapper.toBookingDTO(bookingService.changeApproved(bookingId, userId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingDTO findId(@Valid @PathVariable long bookingId,
                                 @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return BookingMapper.toBookingDTO(bookingService.findBookingById(bookingId, userId));
    }

    @GetMapping()
    public List<BookingDTO> findByUser(@Valid @RequestHeader(value = "X-Sharer-User-Id") long userId,
                                               @RequestParam(value = "state", required = false, defaultValue = "ALL")
                                               String state) {


        return bookingService.findAllForUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDTO> findByOwner(@Valid @RequestParam(value = "state", required = false,
            defaultValue = "ALL") String state,
                                               @RequestHeader("X-Sharer-User-Id")
                                               long ownerId) {
        return bookingService.findBookingsByOwner(ownerId, state);
    }
}
