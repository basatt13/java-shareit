package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentsDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemServiceImpl itemService;

    @PostMapping
    public Item addItems(@RequestHeader("X-Sharer-User-Id") long userId,
                         @Valid @RequestBody ItemDTO item) {
        return itemService.addItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId,
                           @RequestBody ItemDTO item) {
        return itemService.update(item, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDTO getItemById(@PathVariable long itemId,
                               @RequestHeader(value = "X-Sharer-User-Id") long ownerId) {

        return itemService.getItem(itemId, ownerId);
    }

    @GetMapping
    public List<ItemDTO> getAllItems(@RequestHeader("X-Sharer-User-id") long userId) {
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<Item> getItemByText(@RequestParam String text) {
        return itemService.getItemByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentsDTO addComment(@PathVariable("itemId") Long itemId,
                                  @RequestBody CommentsDTO comment,
                                  @RequestHeader("X-Sharer-User-Id") Long userPrincipal)
            throws NoSuchElementException, IllegalArgumentException {
        return itemService.addComment(itemId, comment, userPrincipal);
    }
}
