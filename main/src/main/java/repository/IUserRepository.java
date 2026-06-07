package repository;

import entities.User;

import java.util.List;

public interface IUserRepository {
    User getUser(String login);
    List<User> getUsers();
    void save();
    void load();
    void update(User user);
}
