import auth.Authentication;
import entities.*;
import repository.UserRepositoryImpl;
import repository.VehicleRepositoryImpl;

import java.util.List;
import java.util.Scanner;

public class VehicleRentalApplication {
    public static void main(String[] args) {
        VehicleRepositoryImpl vehicleRepo = new VehicleRepositoryImpl();
        UserRepositoryImpl userRepo = new UserRepositoryImpl();
        Authentication auth = new Authentication(userRepo);
        Scanner scanner = new Scanner(System.in);
        System.out.println("======================================");
        System.out.println("=== Welcome to vehicle rental app! ===");
        System.out.println("======================================");
        System.out.printf("");
        boolean running = true;

        while(running) {
            System.out.println("\n*** MAIN MENU ***");
            System.out.println("1. Log in");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            String mainChoice = scanner.nextLine();

            switch (mainChoice) {
                case "1":
                    System.out.println("Enter login: ");
                    String login = scanner.nextLine();
                    System.out.println("Enter password: ");
                    String password = scanner.nextLine();

                    User loggedInUser = auth.login(login, password);

                    if (loggedInUser != null) {
                        System.out.println("*** LOGGED IN SUCCESSFULLY ***");
                        System.out.println("Your role: " + loggedInUser.role);

                        if (loggedInUser.role == Role.ADMIN) {
                            runAdminMenu(scanner, vehicleRepo, userRepo);
                        } else {
                            runUserMenu(scanner, vehicleRepo, userRepo, loggedInUser);
                        }
                    } else {
                        System.out.println("**! CREDENTIALS ARE INCORRECT, PLEASE, TRY AGAIN !**");
                    }
                    break;

                case "2":
                    System.out.println("\n--- REGISTRATION ---");
                    System.out.print("Enter new login: ");
                    String newLogin = scanner.nextLine();

                    if (userRepo.getUser(newLogin) != null) {
                        System.out.println("User with this login already exists! Choose another one.");
                    } else {
                        System.out.print("Enter new password: ");
                        String newPassword = scanner.nextLine();
                        String hashedPassword = Authentication.hashPassword(newPassword);
                        User newUser = new User.Builder(newLogin, hashedPassword, Role.USER).build();

                        userRepo.add(newUser);
                        userRepo.save();
                        System.out.println("Account created successfully! You can now log in.");
                    }
                    break;

                case "3":
                    System.out.println("See you later!");
                    running = false;
                    break;

                default:
                    System.out.println("Unknown option. Please choose 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    private static void runAdminMenu(Scanner scanner, VehicleRepositoryImpl vehicleRepo, UserRepositoryImpl userRepo) {
        boolean adminMenuRunning = true;
        while (adminMenuRunning) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. See the list of vehicles.");
            System.out.println("2. Add a vehicle (TODO).");
            System.out.println("3. Remove a vehicle.");
            System.out.println("4. See the list of users and their rented vehicles.");
            System.out.println("5. Log out.");
            System.out.println("6. Remove a user.");
            System.out.print("Choose option (1-6): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    List<Vehicle> vehicles = vehicleRepo.getVehicles();
                    for (Vehicle v : vehicles) {
                        System.out.println(v.toString());
                    }
                    break;
                case "2":
                    System.out.println("Adding a vehicle. Enter the type (1 for car, 2 for motorcycle): ");
                    int type = scanner.nextInt();
                    scanner.nextLine();
                    if(type == 1) {
                        System.out.println("Enter the brand: ");
                        String brand = scanner.nextLine();
                        System.out.println("Enter the model: ");
                        String model = scanner.nextLine();
                        System.out.println("Enter year: ");
                        int year = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter price: ");
                        double price = scanner.nextDouble();
                        scanner.nextLine();

                        Car car = new Car.Builder("0", brand, model, year, price, false).build();
                        vehicleRepo.add(car);
                    } else if(type == 2) {
                        System.out.println("Enter the brand: ");
                        String brand = scanner.nextLine();
                        System.out.println("Enter the model: ");
                        String model = scanner.nextLine();
                        System.out.println("Enter year: ");
                        int year = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter price: ");
                        double price = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.println("Enter category: ");
                        String category = scanner.nextLine();

                        Motorcycle moto = new Motorcycle.Builder("0", brand, model, year, price, false, category).build();
                        vehicleRepo.add(moto);
                    }
                    break;
                case "3":
                    System.out.print("Enter ID of the vehicle to be removed: ");
                    String idToRemove = scanner.nextLine();
                    vehicleRepo.remove(idToRemove);
                    vehicleRepo.save();
                    System.out.println("Removed the vehicle (if existed).");
                    break;
                case "4":
                    List<User> users = userRepo.getUsers();
                    for (User u : users) {
                        String vehicleInfo = (u.rentedVehicleId != null) ? u.rentedVehicleId : "no such vehicle";
                        System.out.println("User: " + u.login + " | Rented: " + vehicleInfo);
                    }
                    break;
                case "5":
                    adminMenuRunning = false;
                    System.out.println("*** LOGGED OUT SUCCESSFULLY ***");
                    break;
                case "6":
                    System.out.print("Enter user login to remove: ");
                    String loginToRemove = scanner.nextLine();

                    if (loginToRemove.equals("admin")) {
                        System.out.println("You cannot remove the main admin account!");
                        break;
                    }

                    User userToDelete = userRepo.getUser(loginToRemove);

                    if (userToDelete == null) {
                        System.out.println("No such user found in the database.");
                    } else if (userToDelete.rentedVehicleId != null) {
                        System.out.println("Cannot remove this user. They must return their rented vehicle (" + userToDelete.rentedVehicleId + ") first!");
                    } else {
                        userRepo.remove(loginToRemove);
                        userRepo.save();
                        System.out.println("User removed successfully.");
                    }
                    break;
                default:
                    System.out.println("An unknown option. Please, choose again.");
            }
        }
    }
    private static void runUserMenu(Scanner scanner, VehicleRepositoryImpl vehicleRepo, UserRepositoryImpl userRepo, User user) {
        boolean userMenuRunning = true;
        while (userMenuRunning) {
            System.out.println("\n--- USER MENU ---");
            System.out.println("1. Rent a vehicle.");
            System.out.println("2. Return the vehicle.");
            System.out.println("3. See my data.");
            System.out.println("4. Log out.");
            System.out.print("Choose an option (1-4): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    if (user.rentedVehicleId != null) {
                        System.out.println("You have already rented a vehicle! You must return it to continue.");
                    } else {
                        System.out.print("Provide ID of the vehicle you want to rent: ");
                        List<Vehicle> vehicles = vehicleRepo.getVehicles();
                        for (Vehicle v : vehicles) {
                            System.out.println(v.toString());
                        }
                        System.out.println("Your answer: ");
                        String vehicleId = scanner.nextLine();
                        Vehicle v = vehicleRepo.getVehicle(vehicleId);

                        if (v != null) {
                            vehicleRepo.rentVehicle(v);
                            user.rentedVehicleId = vehicleId;
                            userRepo.update(user);
                            vehicleRepo.save();
                            userRepo.save();
                            System.out.println("Rented a vehicle successfully!");
                        } else {
                            System.out.println("Vehicle with this ID does not exist.");
                        }
                    }
                    break;
                case "2":
                    if (user.rentedVehicleId != null) {
                        vehicleRepo.returnVehicle(user.rentedVehicleId);
                        user.rentedVehicleId = null;

                        userRepo.update(user);
                        vehicleRepo.save();
                        userRepo.save();
                        System.out.println("The vehicle was returned successfully.");
                    } else {
                        System.out.println("You haven't rented a vehicle yet.");
                    }
                    break;
                case "3":
                    System.out.println("=== YOUR DATA ===");
                    System.out.println("Login: " + user.login);
                    System.out.println("Role: " + user.role);
                    if (user.rentedVehicleId != null) {
                        Vehicle myVehicle = vehicleRepo.getVehicle(user.rentedVehicleId);
                        System.out.println("Rented vehicle: " + (myVehicle != null ? myVehicle.toString() : user.rentedVehicleId));
                    } else {
                        System.out.println("Rented vehicle: none");
                    }
                    break;
                case "4":
                    userMenuRunning = false;
                    System.out.println("*** LOGGED OUT SUCCESSFULLY ***");
                    break;
                default:
                    System.out.println("An unknown option, please, choose again.");
            }
        }
    }
}
