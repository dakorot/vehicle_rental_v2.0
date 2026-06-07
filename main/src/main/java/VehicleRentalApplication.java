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
            System.out.println("*** PLEASE, LOG IN TO CONTINUE ***");
            System.out.println("Enter login or \"exit\" to quit:");
            String login = scanner.nextLine();

            if (login.equalsIgnoreCase("exit")) {
                System.out.println("***");
                System.out.println("See you later!");
                System.out.println("***");
                break;
            }

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
            System.out.print("Choose option (1-5): ");

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
                default:
                    System.out.println("An unknown option. Please, choose again.");
            }
        }
    }
    private static void runUserMenu(Scanner scanner, VehicleRepositoryImpl vehicleRepo, UserRepositoryImpl userRepo, User user) {}
}
