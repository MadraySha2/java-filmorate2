package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> userList = new HashMap<>();
    private int id = 0;

    @Override
    public User addUser(User user) {
        id += 1;
        user.setId(id);
        userList.put(id, user);
        return user;
    }

    @Override
    public List<User> getUsersList() {
        return new ArrayList<>(userList.values());
    }

    @Override
    public User getUserById(Integer id) {
        return userList.get(id);
    }

    @Override
    public List<User> getUsersFrendsList(Integer id) {
        User user = userList.get(id);
        if (user.getFriends().isEmpty()) {
            return new ArrayList<>();
        }
        List<User> friendList = new ArrayList<>();
        for (Long friendId : user.getFriends()) {
            friendList.add(userList.get(friendId));
        }
        return friendList;
    }

    @Override
    public List<User> getUsersCommonFriends(Integer id, Integer otherId) {
        User user0 = userList.get(id);
        User user1 = userList.get(otherId);
        if (user0.getFriends().isEmpty() || user1.getFriends().isEmpty()) {
            return new ArrayList<>();
        }
        List<User> commonFriends = new ArrayList<>();
        for (Long friend : user0.getFriends()) {
            if (user1.getFriends().contains(friend)) {
                commonFriends.add(userList.get(id));
            }
        }
        return commonFriends;
    }

    @Override
    public User updUser(User user) {
        if (userList.containsKey(user.getId())) {
            userList.put(user.getId(), user);
        } else {
            throw new RuntimeException("User inst registered!");
        }
        return user;
    }

    @Override
    public User addFriend(Integer id, Integer friendId) {
        if (!userList.containsKey(id) || !userList.containsKey(friendId)) {
            return null;//throw err
        }
        User user = userList.get(id);
        User user1 = userList.get(friendId);
        Set<Long> friends = user.getFriends();
        Set<Long> friends1 = user1.getFriends();
        friends.add((long) id);
        friends1.add((long) id);
        user.setFriends(friends);
        user1.setFriends(friends1);
        return user;
    }

    @Override
    public User deleteFriend(Integer id, Integer friendId) {
        if (!userList.containsKey(id) || !userList.containsKey(friendId)) {
            return null;//throw err
        }
        User user = userList.get(id);
        User user1 = userList.get(friendId);
        Set<Long> friends = user.getFriends();
        Set<Long> friends1 = user1.getFriends();
        friends.remove((long) id);
        friends1.remove((long) id);
        user.setFriends(friends);
        user1.setFriends(friends1);
        return user;
    }
}

