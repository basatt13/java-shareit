package ru.practicum.shareit.user;

public class UserMapper {
    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getName(), user.getEmail());
    }

    public static User toUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        return user;
    }
}
