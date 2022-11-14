package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(User booker, LocalDateTime now);

    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User booker, LocalDateTime now);

    List<Booking> findAllByBookerAndStatusIsOrderByStartDesc(User booker, Status status);

    List<Booking> findAllByBookerOrderByStartDesc(User booker);

    List<Booking> findAllByBookerAndItemIdAndStatus(User booker, long itemId, Status status);

    @Query(nativeQuery = true, value = "SELECT * FROM bookings as b " +
            "LEFT JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ? " +
            "ORDER BY b.start_date DESC")
    List<Booking> findBookingByOwner(long ownerId);

    @Query(nativeQuery = true, value = "SELECT * FROM bookings as b " +
            "LEFT JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ? and now() between b.start_date and b.end_date ORDER BY b.start_date DESC")
    List<Booking> findForOwnerStartEnd(Long ownerId);

    @Query(nativeQuery = true, value = "SELECT * FROM bookings as b " +
            "LEFT JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ? AND b.end_date < now() " +
            "ORDER BY b.start_date DESC")
    List<Booking> findForOwnerPast(Long ownerId);

    @Query(nativeQuery = true, value = "SELECT * FROM bookings as b " +
            "LEFT JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ? AND b.start_date > now()" +
            "ORDER BY b.start_date DESC")
    List<Booking> findForOwnerFuture(Long ownerId);

    @Query(value = "select * " +
            "from bookings " +
            "where booker_id = ? and now() between start_date and end_date", nativeQuery = true)
    List<Booking> getByCurrentStatus(Long bookerId);

    @Query(nativeQuery = true, value = "SELECT * FROM bookings as b " +
            "LEFT JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ? AND b.status = ? " +
            "ORDER BY b.start_date DESC")
    List<Booking> findForOwnerByStatus(Long ownerId, String status);

    @Query(value = "SELECT * from bookings as b join items as i on i.id = b.item_id where i.owner_id = ? and i.id = ? " +
            "and b.end_date < now() and b.status like 'APPROVED' order by b.end_date desc limit 1", nativeQuery = true)
    Booking findLastBooking(long ownerId, long itemId);

    @Query(value = "SELECT * from bookings as b join items as i on b.item_id = i.id where i.owner_id = ? and i.id = ? " +
            "and b.end_date > now() and b.status = 'APPROVED' order by b.end_date desc limit 1", nativeQuery = true)
    Booking findNextBooking(long ownerId, long itemId);

    @Query(value = "SELECT * from bookings WHERE booker_id = ? AND start_date < now() AND item_id = ?",
            nativeQuery = true)
    List<Booking> findPassBookings(Long userId, Long itemId);
}
