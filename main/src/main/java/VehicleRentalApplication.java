import entities.*;
import repository.RentalRepositoryImpl;
import repository.UserRepositoryImpl;
import repository.VehicleRepositoryImpl;
import service.AuthService;
import service.RentalService;
import service.VehicleService;

import java.util.List;
import java.util.Scanner;

public class VehicleRentalApplication {

    public static void main(String[] args) {
        VehicleRepositoryImpl vehicleRepo = new VehicleRepositoryImpl();
        UserRepositoryImpl userRepo = new UserRepositoryImpl();
        RentalRepositoryImpl rentalRepo = new RentalRepositoryImpl();

        AuthService auth = new AuthService(userRepo);
        RentalService rentalService = new RentalService(rentalRepo, vehicleRepo);
        VehicleService vehicleService = new VehicleService(vehicleRepo, rentalService);

        Scanner scanner = new Scanner(System.in);

        System.out.println("======================================");
        System.out.println("=== Welcome to vehicle rental app! ===");
        System.out.println("======================================");

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
                    System.out.print("Enter login: ");
                    String login = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    User loggedInUser = auth.login(login, password);

                    if (loggedInUser != null) {
                        System.out.println("*** LOGGED IN SUCCESSFULLY ***");
                        System.out.println("Your role: " + loggedInUser.role);

                        if (loggedInUser.role == Role.ADMIN) {
                            runAdminMenu(scanner, vehicleRepo, userRepo, rentalService);
                        } else {
                            runUserMenu(scanner, vehicleRepo, rentalService, vehicleService, loggedInUser);
                        }
                    } else {
                        System.out.println("**! CREDENTIALS ARE INCORRECT, PLEASE, TRY AGAIN !**");
                    }
                    break;

                case "2":
                    System.out.println("\n--- REGISTRATION ---");
                    System.out.print("Enter new login: ");
                    String newLogin = scanner.nextLine();
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();

                    boolean isRegistered = auth.register(newLogin, newPassword);

                    if (isRegistered) {
                        userRepo.save();
                        System.out.println("Account created successfully! You can now log in.");
                    } else {
                        System.out.println("User with this login already exists! Choose another one.");
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

    private static void runAdminMenu(Scanner scanner, VehicleRepositoryImpl vehicleRepo, UserRepositoryImpl userRepo, RentalService rentalService) {
        boolean adminMenuRunning = true;
        while (adminMenuRunning) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. See the list of vehicles.");
            System.out.println("2. Add a vehicle.");
            System.out.println("3. Remove a vehicle.");
            System.out.println("4. See the list of users and their rented vehicles.");
            System.out.println("5. Remove a user.");
            System.out.println("6. Log out.");
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
                        System.out.println("Enter price: ");
                        double price = scanner.nextDouble();
                        scanner.nextLine();

                        Car car = new Car.Builder("0", brand, model, year, price).build();
                        vehicleRepo.add(car);
                        vehicleRepo.save();
                        System.out.println("Car added!");

                    } else if(type == 2) {
                        System.out.println("Enter the brand: ");
                        String brand = scanner.nextLine();
                        System.out.println("Enter the model: ");
                        String model = scanner.nextLine();
                        System.out.println("Enter year: ");
                        int year = scanner.nextInt();
                        System.out.println("Enter price: ");
                        double price = scanner.nextDouble();
                        scanner.nextLine();

                        System.out.println("Enter category: ");
                        String category = scanner.nextLine();

                        Motorcycle moto = new Motorcycle.Builder("0", brand, model, year, price, category).build();
                        vehicleRepo.add(moto);
                        vehicleRepo.save();
                        System.out.println("Motorcycle added!");
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
                        Rental userRental = rentalService.getUserRental(u.login);
                        String vehicleInfo = (userRental != null) ? userRental.vehicleId : "no such vehicle";
                        System.out.println("User: " + u.login + " | Rented vehicle ID: " + vehicleInfo);
                    }
                    break;
                case "5":
                    System.out.print("Enter user login to remove: ");
                    String loginToRemove = scanner.nextLine();

                    if (loginToRemove.equals("admin")) {
                        System.out.println("You cannot remove the main admin account!");
                        break;
                    }

                    User userToDelete = userRepo.getUser(loginToRemove);

                    if (userToDelete == null) {
                        System.out.println("No such user found in the database.");
                    } else if (rentalService.getUserRental(loginToRemove) != null) {
                        System.out.println("Cannot remove this user. They must return their rented vehicle first!");
                    } else {
                        userRepo.remove(loginToRemove);
                        userRepo.save();
                        System.out.println("User removed successfully.");
                    }
                    break;
                case "6":
                    adminMenuRunning = false;
                    System.out.println("*** LOGGED OUT SUCCESSFULLY ***");
                    break;
                default:
                    System.out.println("An unknown option. Please, choose again.");
            }
        }
    }

    private static void runUserMenu(Scanner scanner, VehicleRepositoryImpl vehicleRepo, RentalService rentalService, VehicleService vehicleService, User user) {
        boolean userMenuRunning = true;
        while (userMenuRunning) {
            System.out.println("\n--- USER MENU ---");
            System.out.println("1. Rent a vehicle");
            System.out.println("2. Return a vehicle");
            System.out.println("3. See my data");
            System.out.println("4. Log out");
            System.out.print("Choose option (1-4): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    List<Vehicle> available = vehicleService.getAvailableVehicles();
                    if (available.isEmpty()) {
                        System.out.println("Currently there are no available vehicles.");
                    } else {
                        for (Vehicle v : available) {
                            System.out.println(v.toString());
                        }
                    }
                    System.out.print("Enter ID of the vehicle you want to rent: ");
                    String vehicleId = scanner.nextLine();
                    boolean rented = rentalService.rentVehicle(user.login, vehicleId);
                    if (rented) {
                        System.out.println("Vehicle rented successfully!");
                    } else {
                        System.out.println("Cannot rent this vehicle. It might not exist, be already rented, or you already have one.");
                    }
                    break;
                case "2":
                    boolean returned = rentalService.returnVehicle(user.login);
                    if (returned) {
                        System.out.println("Vehicle returned successfully!");
                    } else {
                        System.out.println("You don't have any vehicle to return.");
                    }
                    break;
                case "3":
                    System.out.println("--- YOUR DATA ---");
                    System.out.println("Login: " + user.login);
                    System.out.println("Role: " + user.role);
                    Rental myRental = rentalService.getUserRental(user.login);
                    if (myRental != null) {
                        System.out.println("Rented vehicle ID: " + myRental.vehicleId);
                    } else {
                        System.out.println("Rented vehicle: None");
                    }
                    break;
                case "4":
                    userMenuRunning = false;
                    System.out.println("*** LOGGED OUT SUCCESSFULLY ***");
                    break;
                default:
                    System.out.println("An unknown option. Please, choose again.");
            }
        }
    }
}