package com.jkh98.qna.service;

import com.jkh98.qna.dao.GenericDao;
import com.jkh98.qna.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final GenericDao<User> genericDao;

    @Autowired
    public UserService(@Qualifier("postgres") GenericDao<User> genericDao) {
        this.genericDao = genericDao;
    }

    public int addUser(User user) {
        return genericDao.insert(user);
    }

    public List<User> getAllUsers() {
        return genericDao.selectAll();
    }

    public Optional<User> getUserById(UUID id) {
        return genericDao.selectById(id);
    }

    public int deleteUserById(UUID id) {
        return genericDao.deleteById(id);
    }

    public int updateUserById(UUID id, User user) {
        return genericDao.updateById(id, user);
    }
}
