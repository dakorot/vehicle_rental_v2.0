package repository.impl;

import entities.Role;
import entities.User;
import repository.IUserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryJdbcImpl implements IUserRepository {
    private final String dbUrl;

    public UserRepositoryJdbcImpl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }

    @Override
    public void add(User user) {
        String sql = "INSERT INTO users (login, password, role) VALUES (?, ?, ?) " +
                "ON CONFLICT (login) DO UPDATE SET password = EXCLUDED.password, role = EXCLUDED.role;";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.login);
            pstmt.setString(2, user.password);
            pstmt.setString(3, user.role.name());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Database error (User add): " + e.getMessage());
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET password = ?, role = ? WHERE login = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.password);
            pstmt.setString(2, user.role.name());
            pstmt.setString(3, user.login); // WHERE login = ?

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("User not found: " + user.login);
            }

        } catch (SQLException e) {
            System.out.println("Database error (User update): " + e.getMessage());
        }
    }

    @Override
    public void remove(String login) {
        String sql = "DELETE FROM users WHERE login = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Database error (User remove): " + e.getMessage());
        }
    }

    @Override
    public User getUser(String login) {
        String sql = "SELECT login, password, role FROM users WHERE login = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String pass = rs.getString("password");
                Role role = Role.valueOf(rs.getString("role"));
                return new User.Builder(login, pass, role).build();
            }

        } catch (SQLException e) {
            System.out.println("Database error (User get): " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT login, password, role FROM users";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String login = rs.getString("login");
                String pass = rs.getString("password");
                Role role = Role.valueOf(rs.getString("role"));
                users.add(new User.Builder(login, pass, role).build());
            }

        } catch (SQLException e) {
            System.out.println("Database error (User getAll): " + e.getMessage());
        }
        return users;
    }

    @Override
    public void save() {}
}
