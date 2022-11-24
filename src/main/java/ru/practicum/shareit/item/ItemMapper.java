package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.comment.CommentMapper;

import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {

    public static ItemDTO toItemDTO(Item item) {
        return new ItemDTO(item.getId(), item.getName(), item.getDescription(), item.isAvailable(),
                new ItemDTO.UserForItemClass(item.getOwner().getId(), item.getOwner().getName()),
                null, null, item.getComments().stream()
                .map(CommentMapper::toCommentsDTO)
                .collect(Collectors.toList())
        );
    }

    public static Item toItem(ItemDTORequest itemDTORequest) {
        return new Item(itemDTORequest.getId(), itemDTORequest.getName(), itemDTORequest.getDescription(), itemDTORequest.getAvailable());
    }

    public static Item toItemFromDTO(ItemDTO itemDTORequest) {
        return new Item(itemDTORequest.getId(), itemDTORequest.getName(), itemDTORequest.getDescription(), itemDTORequest.getAvailable());
    }
}
