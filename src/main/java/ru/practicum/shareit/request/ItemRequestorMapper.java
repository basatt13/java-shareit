package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;

@UtilityClass
class ItemRequestMapper {

    public static ItemRequestDTO toItemRequestDTO(ItemRequest itemRequest) {
        UserSpec userUt = new UserSpec(itemRequest.getRequestor().getId(), itemRequest.getRequestor().getName());

        return new ItemRequestDTO(itemRequest.getId(), itemRequest.getDescription(), userUt, itemRequest.getCreated());

    }
}
