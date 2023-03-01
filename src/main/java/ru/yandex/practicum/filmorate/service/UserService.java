package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage storage;
    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public List<User> getUsersList() {
        return storage.getUsersList();
    }
    public List<User> getUsersFrendsList(Integer id){
        return storage.getUsersFrendsList(id);
    }

    public List<User> getUsersCommonFriends(Integer id, Integer otherId){
        return storage.getUsersCommonFriends(id, otherId);
    }

    public User addUser(User user) {
        return storage.addUser(user);
    }

    public User updUser(User user) {
        return storage.updUser(user);
    }

    public User getUserById(Integer id) {
        return storage.getUserById(id);
    }
    public User addFriend(Integer id, Integer friendId){
        return storage.addFriend(id, friendId);
    };
    public User deleteFriend(Integer id, Integer friendId){
        return storage.deleteFriend(id, friendId);
    };

}

