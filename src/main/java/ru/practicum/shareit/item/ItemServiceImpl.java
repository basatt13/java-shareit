package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.Comments;
import ru.practicum.shareit.comment.CommentsDTO;
import ru.practicum.shareit.comment.CommentsRepository;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.NotFoundIdException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.validate.ValidaterForData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ValidaterForData validateUser;
    private final UserRepository userRepository;

    private final ItemMapper itemMapper;

    private final BookingRepository bookingRepository;
    private final BookingService bookingService;
    private final CommentsRepository commentsRepository;

    @Override
    public Item addItem(ItemDTO itemDTO, long id) {
        Item item = ItemMapper.toItem(itemDTO);
        item.setOwner(validateUser.userIdIsPresent(userRepository.findById(id)));
        return itemRepository.save(item);
    }

    @Override
    public Item update(ItemDTO itemDTO, long id, long itemId) {
        Item item = validateUser.itemIdIsPresent(itemRepository.findById(itemId));
        User user = validateUser.userIdIsPresent(userRepository.findById(id));
        if (item.getOwner().getId() == id) {
            item.setOwner(user);
            item.setName(itemDTO.getName() == null ? item.getName() : itemDTO.getName());
            item.setDescription(itemDTO.getDescription() == null ? item.getDescription() : itemDTO.getDescription());
            item.setAvailable(itemDTO.getAvailable() == null ? item.isAvailable() : itemDTO.getAvailable());
        } else {
            throw new NotFoundIdException("Изменять вещь может только её владелец");
        }
        return itemRepository.save(item);
    }

    @Override
    public ItemDTO getItem(long itemId, long ownerId) {
        ItemDTO itemDTO = ItemMapper.toItemDTO(validateUser.itemIdIsPresent(itemRepository.findById(itemId)));
        return setLastAndNext(itemDTO, ownerId);
    }

    @Override
    public List<ItemDTO> getItemsByUserId(long userId) {
        return setLastAndNextBooking(itemRepository.
                findAllByOwner(validateUser.userIdIsPresent(userRepository.findById(userId))), userId).stream()
                .sorted(Comparator.comparing(ItemDTO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemByText(String text) {
        List<Item> itemsResult = new ArrayList<>();
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        } else {
            String lowText = text.toLowerCase();
            for (Item item : itemRepository.findAll()) {
                if ((item.getName().toLowerCase().contains(lowText) || item.getDescription().toLowerCase()
                        .contains(lowText))
                        && item.isAvailable()) {
                    itemsResult.add(item);
                }
            }
        }
        return itemsResult;
    }

    @Override
    public CommentsDTO addComment(Long itemId, CommentsDTO comment, Long userId)
            throws NoSuchElementException, IllegalArgumentException {
        Comments resultComment = new Comments();
        validateUser.isValidateComment(comment);
        Item item = validateUser.itemIdIsPresent(itemRepository.findById(itemId));

        User user = validateUser.userIdIsPresent(userRepository.findById(userId));
        var bookings = bookingService.checkBookingsForItem(itemId, userId);
        if (bookings == null) {
            throw new DataNotFoundException("У пользователя нет ни одной брони на данную вещь");
        }

        var passedBookings = bookingRepository.findPassBookings(userId, itemId);
        if (passedBookings.isEmpty()) {
            throw new DataNotFoundException("Нельзя оставлять комментарии к будущим бронированиям!");
        }

        resultComment.setItem(item);
        resultComment.setAuthor(user);
        resultComment.setText(comment.getText());
        resultComment.setCreated(LocalDateTime.now());
        commentsRepository.save(resultComment);
        return CommentMapper.toCommentsDTO(resultComment);
    }

    public ItemDTO setLastAndNext(ItemDTO itemDTO, long ownerId) {
        Booking lastBooking;
        Booking nextBooking;

        if (itemDTO.getOwner().getId() != ownerId) {
            nextBooking = null;
            lastBooking = null;
        } else {
            lastBooking = bookingRepository.findLastBooking(itemDTO.getOwner().getId(), itemDTO.getId());
            nextBooking = bookingRepository.findNextBooking(itemDTO.getOwner().getId(), itemDTO.getId());
        }

        itemDTO.setLastBooking(lastBooking == null ? null :
                new ItemDTO.BookingForItem(lastBooking.getId(), lastBooking.getBooker().getId()));
        itemDTO.setNextBooking(nextBooking == null ? null :
                new ItemDTO.BookingForItem(nextBooking.getId(), nextBooking.getBooker().getId()));

        return itemDTO;
    }

    private List<ItemDTO> setLastAndNextBooking(List<Item> items, Long userId) {
        return items.stream()
                .map(ItemMapper::toItemDTO)
                .map(dto -> setLastAndNext(dto, userId))
                .collect(Collectors.toList());
    }
}
