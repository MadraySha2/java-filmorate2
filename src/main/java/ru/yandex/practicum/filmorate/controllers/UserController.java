package ru.yandex.practicum.filmorate.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService service;
    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> getUserList() {
        return service.getUsersList();

    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return service.getUserById(id);
    }
    @GetMapping("{id}/friends")
    public List<User> getUserFriendList(@PathVariable Integer id) {
        return service.getUsersFrendsList(id);
    }
    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends (@PathVariable Integer id, @PathVariable Integer otherId){
        return service.getUsersCommonFriends(id,otherId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return service.addUser(user);
    }

    @PutMapping
    public User updUser(@Valid @RequestBody User user) {
        return service.updUser(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return service.addFriend(id, friendId);
    }
    @DeleteMapping("{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return service.deleteFriend(id, friendId);
    }
}
