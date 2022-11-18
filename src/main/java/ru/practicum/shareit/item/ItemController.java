package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentsDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDTO add(@RequestHeader("X-Sharer-User-Id") long userId,
                         @Valid @RequestBody ItemDTO item) {
        return ItemMapper.toItemDTO(itemService.addItem(item, userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDTO update(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId,
                           @RequestBody ItemDTO item) {
        return ItemMapper.toItemDTO(itemService.update(item, userId, itemId));
    }

    @GetMapping("/{itemId}")
    public ItemDTO getById(@Valid @PathVariable long itemId,
                               @RequestHeader(value = "X-Sharer-User-Id") long ownerId) {

        return itemService.getItem(itemId, ownerId);
    }

    @GetMapping
    public List<ItemDTO> getAll(@Valid @RequestHeader("X-Sharer-User-id") long userId) {
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDTO> getByText(@Valid @RequestParam String text) {
        return
                itemService.getItemByText(text).stream()
                        .map(ItemMapper::toItemDTO)
                        .collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/comment")
    public CommentsDTO addComment(@Valid @PathVariable("itemId") Long itemId,
                                  @RequestBody CommentsDTO comment,
                                  @RequestHeader("X-Sharer-User-Id") Long userPrincipal)
            throws NoSuchElementException, IllegalArgumentException {
        return itemService.addComment(itemId, comment, userPrincipal);
    }
}
