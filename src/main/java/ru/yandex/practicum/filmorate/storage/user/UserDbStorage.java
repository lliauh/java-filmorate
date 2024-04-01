package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.model.user.UserRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public User create(User user) {
        user.validate();

        String sql = "INSERT into users(email, login, name, birthday) VALUES (?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setString(4, user.getBirthday().toString());

            return ps;
        }, keyHolder);

        return findUserById(keyHolder.getKey().intValue());
    }

    @Override
    public User update(User user) {
        user.validate();
        findUserById(user.getId());

        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?;";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setString(4, user.getBirthday().toString());
            ps.setString(5, user.getId().toString());

            return ps;
        }, keyHolder);

        return findUserById(keyHolder.getKey().intValue());
    }

    @Override
    public Collection<User> findAll() {
        String sql = "SELECT * FROM users;";

        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public User findUserById(Integer userId) {
        String sql = "SELECT * FROM users WHERE id = ?;";

        Optional<User> user = jdbcTemplate.query(sql, userRowMapper, userId)
                .stream()
                .findFirst();

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException(String.format("Юзер ID: %d не найден", userId));
        }
    }

    @Override
    public void deleteUserById(Integer userId) {
        findUserById(userId);

        String sqlDeleteAllUserLikes = "DELETE FROM likes WHERE user_id = ?;";
        jdbcTemplate.update(sqlDeleteAllUserLikes, userId);

        String sqlDeleteAllUserFriends = "DELETE FROM friends WHERE first_user_id = ? OR second_user_id = ?";
        jdbcTemplate.update(sqlDeleteAllUserFriends, userId, userId);

        String sqlDeleteUserById = "DELETE FROM users WHERE id = ?;";
        jdbcTemplate.update(sqlDeleteUserById, userId);
    }
}