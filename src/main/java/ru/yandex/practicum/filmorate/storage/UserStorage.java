package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getUsersList();

    User getUserById(Integer id);

    List<User> getUsersFrendsList(Integer id);

    List<User> getUsersCommonFriends(Integer id, Integer otherId);

    User addUser(User user);

    User updUser(User user);

    User addFriend(Integer id, Integer friendId);
    User deleteFriend(Integer id, Integer friendId);
}
