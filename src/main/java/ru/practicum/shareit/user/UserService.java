package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.NotFoundIdException;

import java.util.List;

public interface UserService {

    List<User> allUsers();

    User addUser(UserDTO userDTO) throws DataNotFoundException;

    User getUser(long id);

    User updateUser(User user, long id) throws NotFoundIdException;

    void deleteUser(long id);
}
