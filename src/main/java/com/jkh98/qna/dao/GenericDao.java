package com.jkh98.qna.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GenericDao<T> {

    int insert(UUID id, T t);

    default int insert(T t) {
        UUID id = UUID.randomUUID();
        return insert(id, t);
    }

    List<T> selectAll();

    Optional<T> selectById(UUID id);

    int deleteById(UUID id);

    int updateById(UUID id, T t);
}
