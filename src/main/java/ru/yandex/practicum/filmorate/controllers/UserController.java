package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final HashMap<Integer, User> userList = new HashMap<>();
    private int id = 0;

    @GetMapping("/users")
    public List<User> getUserList() {
        if (userList.isEmpty()) {
            return null;
        }
        return userList.values().stream().toList();
    }

    @PostMapping(value = "/add-user")
    public User addUser(@Valid @RequestBody User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        id += 1;
        user.setId(id);
        userList.put(id, user);
        return user;
    }

    @PutMapping(value = "/upd-user")
    public User updUser(@Valid @RequestBody User user) {
        userList.put(user.getId(), user);
        return user;
    }
}
