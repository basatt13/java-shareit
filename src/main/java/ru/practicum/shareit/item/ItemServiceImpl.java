package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ValidaterForData validateUser;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;
    private final CommentsRepository commentsRepository;

    @Override
    @Transactional
    public Item addItem(ItemDTO itemDTO, long id) {
        Item item = ItemMapper.toItem(itemDTO);
        item.setOwner(validateUser.userIdIsPresent(userRepository.findById(id)));
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item update(ItemDTO itemDTO, long id, long itemId) {
        Item item = validateUser.itemIdIsPresent(itemRepository.findById(itemId));
        User user = validateUser.userIdIsPresent(userRepository.findById(id));
        if (item.getOwner().getId() == id) {
            item.setOwner(user);
            item.setName(itemDTO.getName() == null ? item.getName() : itemDTO.getName());
            if (itemDTO.getDescription() == null || itemDTO.getDescription().isBlank())
                item.setDescription(item.getDescription());
            else item.setDescription(itemDTO.getDescription());
            item.setAvailable(itemDTO.getAvailable() == null ? item.isAvailable() : itemDTO.getAvailable());
        } else {
            throw new NotFoundIdException("Изменять вещь может только её владелец");
        }
        return item;
    }

    @Override
    public ItemDTO getItem(long itemId, long ownerId) {
        ItemDTO itemDTO = ItemMapper.toItemDTO(validateUser.itemIdIsPresent(itemRepository.findById(itemId)));

        return setLastAndNext(itemDTO, approvedBookings(getItemsByUserId(ownerId).stream().map(ItemMapper::toItem)
                .collect(toList())));
    }

    @Override
    public List<ItemDTO> getItemsByUserId(long userId) {
        return setLastAndNextBooking(itemRepository.
                findAllByOwner(validateUser.userIdIsPresent(userRepository.findById(userId))).stream()
                .sorted(Comparator.comparing(Item::getId))
                .collect(toList()));
    }

    @Override
    public List<Item> getItemByText(String text) {
        List<Item> itemsResult = new ArrayList<>();
        if (text.isBlank()) {
            return Collections.emptyList();
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
    @Transactional
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

    public ItemDTO setLastAndNext(ItemDTO itemDTO, List<Booking> bookings) {

        Booking lastBooking = bookings.stream()
                .filter(b -> b.getItem().getId() == itemDTO.getId())
                .filter(b -> b.getEnd().isBefore(LocalDateTime.now())).findFirst()
                .orElse(null);

        Booking nextBooking = bookings.stream()
                .filter(b -> b.getItem().getId() == itemDTO.getId())
                .filter(b -> b.getEnd().isAfter(LocalDateTime.now()))
                .reduce((first, second) -> second)
                .orElse(null);


        if (lastBooking == null) itemDTO.setLastBooking(null);
        else itemDTO.setLastBooking(new ItemDTO.BookingForItem(lastBooking.getId(), lastBooking.getBooker().getId()));
        itemDTO.setNextBooking(nextBooking == null ? null :
                new ItemDTO.BookingForItem(nextBooking.getId(), nextBooking.getBooker().getId()));

        return itemDTO;

    }

    public List<ItemDTO> setLastAndNextBooking(List<Item> items) {
        List<Booking> bookings = approvedBookings(items);
        return items.stream()
                .map(ItemMapper::toItemDTO)
                .map(dto -> setLastAndNext(dto, bookings))
                .collect(toList());
    }

    public List<Booking> approvedBookings(List<Item> items) {
        return bookingRepository.findApprovedForItems(items.stream().map(Item::getId).collect(toList()));
    }
}
