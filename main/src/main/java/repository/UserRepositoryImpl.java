package repository;

import entities.Role;
import entities.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements IUserRepository {
    private List<User> users = new ArrayList<>();
    private String fileName;

    public UserRepositoryImpl() {
        this.fileName = "users.txt";
        load();
    }

    public UserRepositoryImpl(String fileName) {
        this.fileName = fileName;
        load();
    }

    @Override
    public User getUser(String login) {
        for (User user : users) {
            if (user.login.equals(login)) {
                return new User(user);
            }
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        List<User> copiedUsers = new ArrayList<>();
        for(User user : users) {
            copiedUsers.add(user);
        }
        return copiedUsers;
    }

    public void save() {
        try(PrintWriter writer = new PrintWriter(new FileWriter(this.fileName, false))) {
            for(User user : users) {
                String vehicleId = (user.rentedVehicleId != null) ? user.rentedVehicleId : "null";

                writer.println(user.login + ";" + user.password + ";" + user.role.name() + ";" + vehicleId);
            }
        } catch (IOException e) {
            System.out.println("Something went wrong during writing into the users.txt file.");
            throw new RuntimeException(e);
        }
    }

    public void load() {
        users.clear();

        try(BufferedReader br = new BufferedReader(new FileReader(this.fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");

                if (data.length >= 3) {
                    String login = data[0];
                    String password = data[1];
                    Role role = Role.valueOf(data[2]);

                    User.Builder builder = new User.Builder(login, password, role);

                    if (data.length == 4 && !data[3].equals("null")) {
                        builder.rentedVehicleId(data[3]);
                    }

                    users.add(builder.build());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File " + this.fileName + " does not exist. Starting with an empty list.");
        } catch (IOException e) {
            System.out.println("Reading error: " + e.getMessage());
        }
    }

    @Override
    public void update(User user) {
        for(int i=0; i<users.size(); ++i) {
            if(users.get(i).login.equals(user.login)) {
                users.set(i, user);
                break;
            }
        }
    }

    @Override
    public void add(User user) {
        users.add(user);
    }

    @Override
    public void remove(String login) {
        users.removeIf(u -> u.login.equals(login));
    }
}
