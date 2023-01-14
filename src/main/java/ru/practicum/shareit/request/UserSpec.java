package ru.practicum.shareit.request;

import lombok.Data;

@Data
public class UserSpec {
    private long id;
    private String name;

    public UserSpec(long id, String name) {
        this.id = id;

        this.name = name;
    }
}
