package ru.yandex.practicum.filmorate.model.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final MpaStorage mpaStorage;

    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film(
                rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"));

        Integer rate = rs.getInt("rate");
        if (rs.wasNull()) {
            rate = null;
        }
        film.setRate(rate);

        Integer ratingId = rs.getInt("rating_id");
        if (rs.wasNull()) {
            film.setMpa(null);
        } else {
            film.setMpa(mpaStorage.findMpaById(ratingId));
        }

        return film;
    }
}