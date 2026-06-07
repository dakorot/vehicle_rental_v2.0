package auth;

import entities.User;
import org.apache.commons.codec.digest.DigestUtils;
import repository.IUserRepository;

public class Authentication {
    private final IUserRepository userRepository;

    public Authentication(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String login, String password) {
        User user = userRepository.getUser(login);

        if (user != null) {
            String hashedPassword = hashPassword(password);

            if(user.password.equals(hashedPassword)) {
                return user;
            }
        }

        return null;
    }

    public static String hashPassword(String password) {
        return DigestUtils.sha256Hex(password);
    }
}
