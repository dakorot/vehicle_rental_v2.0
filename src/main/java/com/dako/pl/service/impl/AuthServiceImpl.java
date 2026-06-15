package com.dako.pl.service.impl;

import com.dako.pl.entities.Role;
import com.dako.pl.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.dako.pl.repository.IUserRepository;
import org.mindrot.jbcrypt.BCrypt;
import com.dako.pl.service.IAuthService;

@Service
@Transactional
public class AuthServiceImpl implements IAuthService {
    private final IUserRepository userRepository;

    public AuthServiceImpl(IUserRepository userRepository) {
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
