package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> userList = new HashMap<>();
    private int id = 0;

    @GetMapping
    public List<User> getUserList() {
        if (userList.isEmpty()) {
            return new ArrayList<>();
        }
        List<User> userListList = new ArrayList<>(userList.values());
        return userListList;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        id += 1;
        user.setId(id);
        userList.put(id, user);
        return user;
    }

    @PutMapping
    public User updUser(@Valid @RequestBody User user) {
        if (userList.containsKey(user.getId())) {
            userList.put(user.getId(), user);
        } else {
            throw new RuntimeException("User inst registered!");
        }
        return user;
    }
}
