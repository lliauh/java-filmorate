package ru.yandex.practicum.filmorate.model.like;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikeRowMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Like(
                rs.getInt("film_id"),
                rs.getInt("user_id"));
    }
}
