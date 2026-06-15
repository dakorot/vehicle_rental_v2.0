package com.dako.pl.service;

import com.dako.pl.entities.User;

import java.util.List;

public interface IUserService {
    void removeUser(String login);
    List<User> getAllUsers();
    User getUser(String userId);
}
