package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comment.CommentsDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
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
       private long id;
       private long bookerId;
    }

}
