package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.DuplicateException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    List<User> getUsersList();

    User getUserById(Integer id);

    User addUser(User user);

    User updUser(User user);

    List<User> getUsersFrendsList(Integer id);

    Set<User> getUsersCommonFriends(Integer id, Integer otherId);

    User addFriend(Integer id, Integer friendId) throws DuplicateException;

    User deleteFriend(Integer id, Integer friendId) throws DuplicateException;
}
