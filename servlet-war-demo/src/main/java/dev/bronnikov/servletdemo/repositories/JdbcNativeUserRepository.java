package dev.bronnikov.servletdemo.repositories;

import dev.bronnikov.servletdemo.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcNativeUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
                "select id, first_name, last_name, age, active from users",
                (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getBoolean("active")
                ));
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> users = jdbcTemplate.query(
                "select id, first_name, last_name, age, active from users where id = ?",
                (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getBoolean("active")
                ),
                id
        );
        return users.stream().findFirst();
    }

    @Override
    public User save(User user) {
        return jdbcTemplate.queryForObject(
                """
                        insert into users(first_name, last_name, age, active)
                        values(?, ?, ?, ?)
                        returning id, first_name, last_name, age, active
                        """,
                (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("age"),
                        rs.getBoolean("active")
                ),
                user.getFirstName(), user.getLastName(), user.getAge(), user.isActive()
        );
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from users where id = ?", id);
    }
}
