package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comment.CommentsDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private long id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String description;
    @NotNull
    private Boolean available;
    private UserForItemClass owner;
    private BookingForItem lastBooking;
    private BookingForItem nextBooking;
    private List<CommentsDTO> comments;


    @Data
    @AllArgsConstructor
    public static class UserForItemClass {
        private long id;
        private String name;

    }

    @Data
    @AllArgsConstructor
    public static class BookingForItem {
        long id;
        long bookerId;
    }

}
