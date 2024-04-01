package ru.yandex.practicum.filmorate.model.friendship;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendshipRowMapper implements RowMapper<Friendship> {
    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Friendship(
                rs.getInt("first_user_id"),
                rs.getInt("second_user_id"));
    }
}
