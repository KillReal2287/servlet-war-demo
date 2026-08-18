package dev.bronnikov.servletdemo.service;

import dev.bronnikov.servletdemo.User;
import dev.bronnikov.servletdemo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserServiceWithMockedRepoTest.TestConfig.class)
public class UserServiceWithMockedRepoTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Mockito.reset(userRepository);
    }

    @Test
    void testFindUserById() {
        User mockUser = new User(1L, "John", "Doe", 30, true);
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));

        User user = userService.findUserById(1L);
        assertNotNull(user, "Пользователь не должен быть null");
        assertEquals("John", user.getFirstName(), "Имя пользователя не соответствует ожидаемому");
        assertEquals("Doe", user.getLastName(), "Фамилия пользователя не соответствует ожидаемой");
    }

    @Test
    void testCreateUser() {
        User newUser = new User(null, "Jane", "Smith", 27, true);
        User savedUser = new User(2L, "Jane", "Smith", 27, true);

        Mockito.when(userRepository.save(newUser))
                .thenReturn(savedUser);

        User result = userService.createUser(newUser);
        assertNotNull(result.getId(), "Идентификатор должен быть присвоен");
        assertEquals("Jane", result.getFirstName(), "Имя пользователя должно совпадать");
        assertEquals("Smith", result.getLastName(), "Фамилия пользователя должна совпадать");
    }

    @Configuration
    static class TestConfig {

        @Bean
        public UserService userService(UserRepository userRepository) {
            return new UserService(userRepository);
        }

        @Bean
        public UserRepository userRepository() {
            return mock(UserRepository.class);
        }
    }
}
