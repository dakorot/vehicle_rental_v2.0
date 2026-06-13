package repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entities.User;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements IUserRepository {
    private List<User> users = new ArrayList<>();
    private String fileName;

    public UserRepositoryImpl() {
        this.fileName = "users.json";
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
        Gson gson = new Gson();
        try(FileWriter writer = new FileWriter(this.fileName)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.out.println("Something went wrong during writing into the users.json file.");
            throw new RuntimeException(e);
        }
    }

    public void load() {
        Gson gson = new Gson();

        try(FileReader reader = new FileReader(this.fileName)) {
            Type listType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> loadedUsers = gson.fromJson(reader, listType);

            if (loadedUsers != null) this.users = loadedUsers;
            else this.users = new ArrayList<>();

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
