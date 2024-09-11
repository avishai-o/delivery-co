package dev.ao.example.controllers;

import dev.ao.example.models.User;
import dev.ao.example.services.AddressService;
import dev.ao.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;

    public UserController(UserService userService,
                          AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    //   TODO for admin and test roles use only
    @GetMapping("/")
    List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    User getUserById(@PathVariable String id) throws Exception {
        return userService.findUsersByIdWrapper(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/create")
    void createUser(@Validated @RequestBody User user) throws Exception {
        userService.saveOrUpdateUser(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/update/{userId}/address/{addressId}")
    void updateUserAddress(@Validated @PathVariable String userId, @PathVariable String addressId) throws Exception {
        User user = userService.findUsersByIdWrapper(userId);
        user.setAddress(addressService.findAddressesByIdWrapper(addressId));
        userService.saveOrUpdateUser(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/update")
    void updateUser(@Validated @RequestBody User user) {
        userService.saveOrUpdateUser(user);
    }

    @DeleteMapping("/{id}")
    void deleteUserById(@PathVariable String id) {
        userService.deleteUserById(id);
    }
}
