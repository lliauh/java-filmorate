package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.friendship.Friendship;
import ru.yandex.practicum.filmorate.model.friendship.FriendshipRowMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendshipRowMapper friendshipRowMapper;

    @Override
    public void addFriends(Integer firstUserId, Integer secondUserId) {
        if (friendshipAlreadyExists(firstUserId, secondUserId)) {
            log.debug("Дружба между пользователями {} и {} уже существует.", firstUserId, secondUserId);
            throw new AlreadyExistsException(String.format("Дружба между ID: %d и ID: %d уже существует.",
                    firstUserId, secondUserId));
        } else {
            String sql = "INSERT into friends(first_user_id, second_user_id) VALUES (?, ?);";
            jdbcTemplate.update(sql, firstUserId, secondUserId);
        }
    }

    @Override
    public void deleteFriends(Integer firstUserId, Integer secondUserId) {
        if (friendshipAlreadyExists(firstUserId, secondUserId)) {
            String sql = "DELETE FROM friends WHERE (first_user_id = ? AND second_user_id = ?);";
            jdbcTemplate.update(sql, firstUserId, secondUserId);
        }
    }

    @Override
    public List<Friendship> findFriendsByUserId(Integer userId) {
        String sql = "SELECT * FROM friends WHERE first_user_id = ?;";

        return jdbcTemplate.query(sql, friendshipRowMapper, userId)
                .stream()
                .collect(Collectors.toList());
    }

    public boolean friendshipAlreadyExists(Integer firstUserId, Integer secondUserId) {
        List<Friendship> firstUserFriends;

        try {
            firstUserFriends = findFriendsByUserId(firstUserId);
        } catch (NotFoundException e) {
            return false;
        }

        for (Friendship friendship : firstUserFriends) {
            if ((friendship.getFirstUserId().equals(firstUserId)
                    && friendship.getSecondUserId().equals(secondUserId))) {
                return true;
            }
        }

        return false;
    }
}
