package com.dako.pl.service;

import com.dako.pl.entities.User;

public interface IAuthService {
    User login(String login, String password);
    boolean register(String login, String password);
}
