package service;

import entities.User;

public interface IAuthService {
    User login(String login, String password);
    boolean register(String login, String password);
}
