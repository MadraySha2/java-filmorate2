package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.DuplicateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDBTest {
    private final UserDB userStorage;


    @Test
    public void testGetAllUsers() throws DuplicateException {
        userStorage.addUser(User.builder()
                .login("Test")
                .birthday(LocalDate.now())
                .name("TEST")
                .email("test@ya.ru")
                .build());
        userStorage.addUser(User.builder()
                .login("Test1")
                .birthday(LocalDate.now())
                .name("TEST1")
                .email("test1@ya.ru")
                .build());
        userStorage.addUser(User.builder()
                .login("Test2")
                .birthday(LocalDate.now())
                .name("TEST2")
                .email("test2@ya.ru")
                .build());

        List<User> users = userStorage.getUsersList();
        assertThat(users.get(0).getName()).isEqualTo("TEST");
        assertThat(users.get(1).getLogin()).isEqualTo("Test1");
        assertThat(users.get(2).getId()).isEqualTo(3);
    }

    @Test
    public void testFindUserById() throws DuplicateException {
        userStorage.addUser(User.builder()
                .login("Test")
                .birthday(LocalDate.now())
                .name("TEST")
                .email("test@ya.ru")
                .build());
        User user1 = userStorage.getUserById(1);
        assertThat(user1).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    public void testUserUpdate() throws DuplicateException {
        userStorage.addUser(User.builder()
                .login("Test")
                .birthday(LocalDate.now())
                .name("TEST")
                .email("test@ya.ru")
                .build());
        User user = userStorage.getUserById(1);
        user.setLogin("TEST1");
        userStorage.updateUser(user);
        assertThat(userStorage.getUserById(1).getLogin()).isEqualTo("TEST1");
    }
}