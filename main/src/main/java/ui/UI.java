package ui;

import entities.Rental;
import entities.Role;
import entities.User;
import entities.Vehicle;
import entities.VehicleCategoryConfig;
import repository.impl.UserRepositoryImpl;
import service.*;

import java.util.List;
import java.util.Scanner;

public class UI {

    private final AuthService authService;
    private final RentalService rentalService;
    private final VehicleService vehicleService;
    private final VehicleCategoryConfigService configService;
    private final UserService userService;
    private final UserRepositoryImpl userRepo;
    private final Scanner scanner = new Scanner(System.in);

    public UI(AuthService authService, RentalService rentalService, VehicleService vehicleService,
              VehicleCategoryConfigService configService, UserService userService, UserRepositoryImpl userRepo) {
        this.authService = authService;
        this.rentalService = rentalService;
        this.vehicleService = vehicleService;
        this.configService = configService;
        this.userService = userService;
        this.userRepo = userRepo;
    }

    public void start() {
        System.out.println("======================================");
        System.out.println("=== Welcome to vehicle rental app! ===");
        System.out.println("======================================");

        boolean running = true;
        while (running) {
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

                    User loggedInUser = authService.login(login, password);

                    if (loggedInUser != null) {
                        System.out.println("*** LOGGED IN SUCCESSFULLY ***");
                        System.out.println("Your role: " + loggedInUser.role);

                        if (loggedInUser.role == Role.ADMIN) {
                            runAdminMenu();
                        } else {
                            runUserMenu(loggedInUser);
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

                    boolean isRegistered = authService.register(newLogin, newPassword);
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
    }

    private void runAdminMenu() {
        boolean adminMenuRunning = true;
        while (adminMenuRunning) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. See the list of vehicles.");
            System.out.println("2. Add a vehicle (Config-Driven).");
            System.out.println("3. Remove a vehicle.");
            System.out.println("4. See the list of users and their rented vehicles.");
            System.out.println("5. Remove a user.");
            System.out.println("6. Log out.");
            System.out.print("Choose option (1-6): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    List<Vehicle> vehicles = vehicleService.getAllVehicles();
                    for (Vehicle v : vehicles) {
                        System.out.println(v.toString());
                    }
                    break;
                case "2":
                    System.out.println("Available categories:");
                    configService.findAllCategories().forEach(c -> System.out.println("- " + c.getCategory()));

                    try {
                        System.out.print("\nProvide a category: ");
                        VehicleCategoryConfig config = configService.getByCategory(scanner.nextLine().trim());

                        System.out.print("Provide a brand: ");
                        String brand = scanner.nextLine().trim();

                        System.out.print("Provide a model: ");
                        String model = scanner.nextLine().trim();

                        System.out.print("Provide a year: ");
                        int year = Integer.parseInt(scanner.nextLine().trim());

                        System.out.print("Provide a price: ");
                        double price = Double.parseDouble(scanner.nextLine().trim());

                        Vehicle vehicle = Vehicle.builder()
                                .id("0")
                                .category(config.getCategory())
                                .brand(brand)
                                .model(model)
                                .year(year)
                                .price(price)
                                .build();

                        config.getAttributes().forEach((attrName, attrType) -> {
                            System.out.print("Provide value for the attribute " + attrName + " (" + attrType + "): ");
                            String rawValue = scanner.nextLine().trim();

                            Object value = switch (attrType.toLowerCase()) {
                                case "string" -> rawValue;
                                case "integer" -> Integer.parseInt(rawValue);
                                case "number" -> Double.parseDouble(rawValue);
                                case "boolean" -> Boolean.parseBoolean(rawValue);
                                default -> throw new IllegalArgumentException("Nieobsługiwany typ: " + attrType);
                            };
                            vehicle.addAttribute(attrName, value);
                        });

                        Vehicle added = vehicleService.addVehicle(vehicle);
                        System.out.println("Vehicle added successfully!:\n" + added);

                    } catch (Exception e) {
                        System.out.println("An error occurred when trying to add a vehicle: " + e.getMessage());
                    }
                    break;
                case "3":
                    System.out.print("Enter ID of the vehicle to be removed: ");
                    String idToRemove = scanner.nextLine();
                    try {
                        vehicleService.removeVehicle(idToRemove);
                        System.out.println("Removed the vehicle.");
                    } catch (Exception e) {
                        System.out.println("An error occurred when trying to remove a vehicle: " + e.getMessage());
                    }
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
                    try {
                        userService.removeUser(loginToRemove);
                        System.out.println("User removed successfully.");
                    } catch (Exception e) {
                        System.out.println("An error occurred when trying to remove a user: " + e.getMessage());
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

    private void runUserMenu(User user) {
        boolean userMenuRunning = true;
        while (userMenuRunning) {
            System.out.println("\n--- USER MENU ---");
            System.out.println("1. See available vehicles");
            System.out.println("2. Rent a vehicle");
            System.out.println("3. Return a vehicle");
            System.out.println("4. See my data");
            System.out.println("5. Log out");
            System.out.print("Choose option (1-5): ");

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
                    break;
                case "2":
                    System.out.print("Enter ID of the vehicle you want to rent: ");
                    List<Vehicle> vehicles = vehicleService.getAllVehicles();
                    for (Vehicle v : vehicles) {
                        System.out.println(v.toString());
                    }
                    String vehicleId = scanner.nextLine();
                    boolean rented = rentalService.rentVehicle(user.login, vehicleId);
                    if (rented) {
                        System.out.println("Vehicle rented successfully!");
                    } else {
                        System.out.println("Cannot rent this vehicle. It might not exist, be already rented, or you already have one.");
                    }
                    break;
                case "3":
                    boolean returned = rentalService.returnVehicle(user.login);
                    if (returned) {
                        System.out.println("Vehicle returned successfully!");
                    } else {
                        System.out.println("You don't have any vehicle to return.");
                    }
                    break;
                case "4":
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
                case "5":
                    userMenuRunning = false;
                    System.out.println("*** LOGGED OUT SUCCESSFULLY ***");
                    break;
                default:
                    System.out.println("An unknown option. Please, choose again.");
            }
        }
    }
}