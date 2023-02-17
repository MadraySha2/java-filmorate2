package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
        return new ArrayList<>(userList.values());
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
