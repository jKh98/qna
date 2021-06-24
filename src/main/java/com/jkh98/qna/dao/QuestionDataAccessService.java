package com.jkh98.qna.dao;

import com.jkh98.qna.model.Question;
import com.jkh98.qna.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuestionDataAccessService implements GenericDao<Question> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public QuestionDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insert(UUID id, Question question) {
        String sql = "INSERT INTO" +
                " person ( id, title, body, categoryId, userId ) " +
                " VALUES (?, ?, ? , ?, ?)";
        return jdbcTemplate.update(
                sql,
                id,
                question.getTitle(),
                question.getBody(),
                question.getCategoryId(),
                question.getUserId());
    }

    @Override
    public int insert(Question question) {
        return GenericDao.super.insert(question);
    }

    @Override
    public List<Question> selectAll() {
        final String sql = "SELECT id, title, body, categoryId, userId FROM question";
        return jdbcTemplate.query(sql, mapQuestionFomDb());
    }

    @Override
    public Optional<Question> selectById(UUID id) {
        return Optional.empty();
    }

    @Override
    public int deleteById(UUID id) {
        return 0;
    }

    @Override
    public int updateById(UUID id, Question question) {
        return 0;
    }

    private RowMapper<Question> mapQuestionFomDb() {
        return ((resultSet, i) ->
                new Question(
                        UUID.fromString(resultSet.getString("id")),
                        resultSet.getString("title"),
                        resultSet.getString("body"),
                        UUID.fromString(resultSet.getString("categoryId")),
                        UUID.fromString(resultSet.getString("userId"))
                )
        );
    }
}
