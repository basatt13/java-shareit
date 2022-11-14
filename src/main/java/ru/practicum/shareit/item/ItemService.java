package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.CommentsDTO;

import java.util.List;

public interface ItemService {

    Item addItem(ItemDTO itemDTO, long id);

    Item update(ItemDTO itemDTO, long id, long itemId);

    ItemDTO getItem(long ownerId, long itemId);

    List<ItemDTO> getItemsByUserId(long UserId);

    List<Item> getItemByText(String text);

    CommentsDTO addComment(Long itemId, CommentsDTO comment, Long userId);
}
