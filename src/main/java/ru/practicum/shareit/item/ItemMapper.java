package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.comment.CommentMapper;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemMapper {

    public static ItemDTO toItemDTO(Item item) {
        return new ItemDTO(item.getId(), item.getName(), item.getDescription(), item.isAvailable(),
                new ItemDTO.UserForItemClass(item.getOwner().getId(), item.getOwner().getName()),
                null, null, item.getComments().stream()
                .map(CommentMapper::toCommentsDTO)
                .collect(Collectors.toList())
        );
    }

    public static Item toItem(ItemDTO itemDTO) {
        return new Item(itemDTO.getId(), itemDTO.getName(), itemDTO.getDescription(), itemDTO.getAvailable());
    }
}
