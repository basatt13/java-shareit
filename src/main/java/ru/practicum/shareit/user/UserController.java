package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    List<UserDTO> getAll() {
        return userService.allUsers().stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    UserDTO get(@PathVariable long id) {
        return UserMapper.toUserDTO(userService.getUser(id));
    }

    @PostMapping
    UserDTO add(@Valid @RequestBody UserDTO userDTO) {
        return UserMapper.toUserDTO(userService.addUser(userDTO));
    }

    @PatchMapping("/{userId}")
    UserDTO update(@Valid @RequestBody User user,
                   @PathVariable long userId) {
        return UserMapper.toUserDTO(userService.updateUser(user, userId));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable long id) {
        userService.deleteUser(id);
    }
}
