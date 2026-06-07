package auth;

import entities.User;
import org.junit.jupiter.api.Test;
import repository.UserRepositoryImpl;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTest {

    @Test
    public void testHashPasswordGeneratesCorrectLength() {
        String rawPassword = "admin";
        String hashedPassword = Authentication.hashPassword(rawPassword);

        assertNotNull(hashedPassword);
        assertEquals(64, hashedPassword.length());
        assertNotEquals(rawPassword, hashedPassword);
    }

    @Test
    public void testLoginFailsForUnknownUser() {
        UserRepositoryImpl userRepo = new UserRepositoryImpl("usersTest.txt");
        Authentication auth = new Authentication(userRepo);

        User result = auth.login("wrongLogin", "somePassword");
        assertNull(result);
    }
}