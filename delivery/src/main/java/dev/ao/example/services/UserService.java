package dev.ao.example.services;


import dev.ao.example.models.User;
import dev.ao.example.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    public Optional<User> findUsersById(String id) {
        return userRepository.findById(id);
    }

    public User findUsersByIdWrapper(String id) throws Exception {
        Optional<User> user = findUsersById(id);
        if (user.isEmpty()) {
            throw new Exception();
        }
        return user.get();
    }

    public void saveOrUpdateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }
}
