package com.jkh98.qna.dao;

import com.jkh98.qna.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDataAccessService implements GenericDao<Category> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insert(UUID id, Category category) {
        String sql = "INSERT INTO category ( id, name )  VALUES (?, ?)";

        return jdbcTemplate.update(sql, id, category.getName());
    }

    @Override
    public int insert(Category category) {
        return GenericDao.super.insert(category);
    }

    @Override
    public List<Category> selectAll() {
        final String sql = "SELECT id, name FROM category";
        return jdbcTemplate.query(sql, mapCategoryFomDb());
    }

    @Override
    public Optional<Category> selectById(UUID id) {
        final String sql = "SELECT id, name FROM category WHERE id = ?";
        List<Category> categories = jdbcTemplate.query(sql, mapCategoryFomDb(), id);

        return Optional.ofNullable(categories.isEmpty() ? null : categories.get(0));
    }

    @Override
    public int deleteById(UUID id) {
        String sql = "DELETE FROM category WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }

    @Override
    public int updateById(UUID id, Category category) {
        String sql = "UPDATE category SET name = ? WHERE id = ?";

        return jdbcTemplate.update(sql, category.getName(), id);
    }

    private RowMapper<Category> mapCategoryFomDb() {
        return ((resultSet, i) ->
                new Category(
                        UUID.fromString(resultSet.getString("id")),
                        resultSet.getString("name")
                )
        );
    }
}
