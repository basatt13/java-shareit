package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.UserUt;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDTO {
    private long id;
    private String description;
    private UserUt requestor;
    private LocalDate created;
}
