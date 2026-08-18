package dev.bronnikov.servletdemo.repositories;

import dev.bronnikov.servletdemo.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    void deleteById(Long id);
}
