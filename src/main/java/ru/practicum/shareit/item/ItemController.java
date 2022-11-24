package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentsDTO;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDTO add(@RequestHeader("X-Sharer-User-Id") long userId,
                       @Valid @RequestBody ItemDTORequest item) {
        return ItemMapper.toItemDTO(itemService.addItem(item, userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDTO update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long itemId,
                          @RequestBody ItemDTORequest item) {
        return ItemMapper.toItemDTO(itemService.update(item, userId, itemId));
    }

    @GetMapping("/{itemId}")
    public ItemDTO getById(@PathVariable long itemId,
                           @RequestHeader(value = "X-Sharer-User-Id") long ownerId) {

        return itemService.getItem(itemId, ownerId);
    }

    @GetMapping
    public List<ItemDTO> getAll(@RequestHeader("X-Sharer-User-id") long userId) {
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDTO> getByText(@RequestParam String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        } else {
            return
                    itemService.getItemByText(text).stream()
                            .map(ItemMapper::toItemDTO)
                            .collect(Collectors.toList());
        }
    }

    @PostMapping("/{itemId}/comment")
    public CommentsDTO addComment(@PathVariable("itemId") Long itemId,
                                  @Valid @RequestBody CommentsDTO comment,
                                  @RequestHeader("X-Sharer-User-Id") Long userPrincipal) {
        return itemService.addComment(itemId, comment, userPrincipal);
    }
}
