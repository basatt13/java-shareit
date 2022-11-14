package ru.practicum.shareit;

import lombok.Data;

@Data
public class UserUt {
    private long id;
    private String name;

    public UserUt(long id, String name) {
        this.id = id;

        this.name = name;
    }
}
