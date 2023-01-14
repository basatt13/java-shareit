package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.comment.CommentsDTO;

import java.util.List;

public interface ItemService {

    Item addItem(ItemDTORequest itemDTO, long id);

    Item update(ItemDTORequest itemDTO, long id, long itemId);

    ItemDTO getItem(long ownerId, long itemId);

    List<ItemDTO> getItemsByUserId(long UserId);

    List<Item> getItemByText(String text);

    CommentsDTO addComment(Long itemId, CommentsDTO comment, Long userId);

    List<Booking> approvedBookings(List<Item> items);
}
