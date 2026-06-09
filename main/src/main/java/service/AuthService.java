package service;

import entities.Role;
import entities.User;
import repository.IUserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private final IUserRepository userRepository;

    public AuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String login, String password) {
        User user = userRepository.getUser(login);

        if (user != null && BCrypt.checkpw(password, user.password)) {
            return user;
        }

        return null;
    }

    public boolean register(String login, String password) {
        if (userRepository.getUser(login) != null) {
            return false;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User.Builder(login, hashedPassword, Role.USER).build();
        userRepository.add(newUser);

        return true;
    }
}
