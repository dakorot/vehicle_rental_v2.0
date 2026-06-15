package com.dako.pl.repository;

import com.dako.pl.entities.User;

import java.util.List;

public interface IUserRepository {
    User getUser(String login);
    List<User> getUsers();
    void update(User user);
    void add(User user);
    void remove(String login);

    void save();
}
