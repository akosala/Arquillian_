package com.infoshareacademy.searchengine.repository;

import com.infoshareacademy.searchengine.domain.User;

import java.util.List;

public interface UsersRepository {
    boolean addUser(User user);

    User getUserById(int id);

    User getUserByLogin(String login);

    List<User> getUsersList();
}
