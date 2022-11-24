package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDTO {

    @NotNull
    private long id;
    @NotBlank
    private String description;
    private UserSpec requestor;
    private LocalDate created;
}
