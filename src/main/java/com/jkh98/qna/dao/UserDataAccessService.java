package com.jkh98.qna.dao;

import com.jkh98.qna.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class UserDataAccessService implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertUser(UUID id, User user) {
        String sql = "INSERT INTO person ( id, username )  VALUES (?, ?)";

        return jdbcTemplate.update(sql, id, user.getUsername());
    }

    @Override
    public int insertUser(User user) {
        return UserDao.super.insertUser(user);
    }

    @Override
    public List<User> selectAllUsers() {
        final String sql = "SELECT id, username FROM person";
        return jdbcTemplate.query(sql, mapUserFomDb());
    }

    @Override
    public Optional<User> selectUserById(UUID id) {
        final String sql = "SELECT id, username FROM person WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, mapUserFomDb(), id);

        return Optional.ofNullable(users.isEmpty() ? null : users.get(0));
    }

    @Override
    public int deleteUserById(UUID id) {
        String sql = "DELETE FROM person WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }

    @Override
    public int updateUserById(UUID id, User user) {
        String sql = "UPDATE person SET username = ? WHERE id = ?";

        return jdbcTemplate.update(sql, user.getUsername(), id);
    }

    private RowMapper<User> mapUserFomDb() {
        return ((resultSet, i) ->
                new User(
                        UUID.fromString(resultSet.getString("id")),
                        resultSet.getString("username")
                )
        );
    }
}
