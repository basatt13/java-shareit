package ru.practicum.shareit.request;


import ru.practicum.shareit.UserUt;

class ItemRequestMapper {

    public ItemRequestDTO toItemRequestDTO(ItemRequest itemRequest) {
        UserUt userUt = new UserUt(itemRequest.getRequestor().getId(), itemRequest.getRequestor().getName());

        return new ItemRequestDTO(itemRequest.getId(), itemRequest.getDescription(), userUt, itemRequest.getCreated());

    }
}
