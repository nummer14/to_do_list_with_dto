package com.example.to_do_list_with_dto.repository;

import com.example.to_do_list_with_dto.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (resultSet, rowNum) -> {
        User user = User.builder()
                .id(resultSet.getInt("id"))
                .username(resultSet.getString("username"))
                .password(resultSet.getString("password"))
                .build();

        return user;
    };

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int save(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
    }
}