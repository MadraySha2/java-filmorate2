package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DuplicateException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> userList = new HashMap<>();
    private Integer id = 0;

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        id += 1;
        user.setId(id);
        userList.put(user.getId(), user);
        System.out.println(id);
        return user;
    }

    @Override
    public List<User> getUsersList() {
        return new ArrayList<>(userList.values());
    }

    @Override
    public User getUserById(Integer id) {
        if (!userList.containsKey(id)) {
            throw new NotFoundException("User not found!");
        }
        return userList.get(id);
    }

    @Override
    public User updUser(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        if (userList.containsKey(user.getId())) {
            userList.put(user.getId(), user);
        } else {
            throw new NotFoundException("User inst registered!");
        }
        return user;
    }

    @Override
    public List<User> getUsersFrendsList(Integer id) {
        User user = getUserById(id);
        List<User> friendList = new ArrayList<>();
        for (Integer friendId : user.getFriends()) {
            friendList.add(getUserById(friendId));
        }
        return friendList;
    }

    @Override
    public Set<User> getUsersCommonFriends(Integer id, Integer otherId) {
        User user0 = getUserById(id);
        User user1 = getUserById(otherId);
        Set<User> commonFriends = new HashSet<>();
        for (Integer friend : user0.getFriends()) {
            if (user1.getFriends().contains(friend)) {
                commonFriends.add(getUserById(friend));
            }
        }
        return commonFriends;
    }

    @Override
    public User addFriend(Integer id, Integer friendId) throws DuplicateException {
        if (id == friendId) {
            throw new DuplicateException("User can't add own page to friends!");
        }
        User user = getUserById(id);
        User user1 = getUserById(friendId);
        user.getFriends().add(friendId);
        user1.getFriends().add(id);
        return user;
    }

    @Override
    public User deleteFriend(Integer id, Integer friendId) throws DuplicateException {
        User user = getUserById(id);
        User user1 = getUserById(friendId);
        if (!user.getFriends().contains(friendId) || !user1.getFriends().contains(id)) {
            throw new DuplicateException("Users not friends yet!");
        }
        user.getFriends().remove(friendId);
        user1.getFriends().remove(id);
        return user;
    }

}

