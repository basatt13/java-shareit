package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundIdException;
import ru.practicum.shareit.validate.ValidaterForData;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ValidaterForData validateUser;

    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Override
    public User addUser(UserDTO userDTO) {
        User user = UserMapper.toUser(userDTO);
        return userRepository.save(user);
    }

    @Override
    public User getUser(long id) {
        return validateUser.userIdIsPresent(userRepository.findById(id));
    }

    @Override
    public User updateUser(User user, long id) throws NotFoundIdException {
        User userOld = validateUser.userIdIsPresent(userRepository.findById(id));
        User newUser = new User(id, user.getName() == null ? userOld.getName() : user.getName(),
                user.getEmail() == null ? userOld.getEmail() : user.getEmail());
        return userRepository.save(newUser);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
