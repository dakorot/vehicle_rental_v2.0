import repository.*;
import repository.impl.*;
import service.*;
import ui.UI;

public class VehicleRentalApplication {

    public static void main(String[] args) {
        VehicleCategoryConfigRepository configRepo = new VehicleCategoryConfigRepositoryImpl();
        VehicleCategoryConfigService configService = new VehicleCategoryConfigService(configRepo);
        VehicleValidator validator = new VehicleValidator(configService);

        String mode = "json";
        if (args.length > 0) {
            mode = args[0].toLowerCase();
        }

        IUserRepository userRepo;
        IVehicleRepository vehicleRepo;
        IRentalRepository rentalRepo;

        if (mode.equals("jdbc")) {
            System.out.println("=== RUNNING THE APP IN JDBC MODE ===");
            String dbUrl = System.getenv("DB_URL");
            userRepo = new UserRepositoryJdbcImpl(dbUrl);
            vehicleRepo = new VehicleRepositoryJdbcImpl(dbUrl);
            rentalRepo = new RentalRepositoryJdbcImpl(dbUrl);
        }
        else {
            System.out.println("=== RUNNING THE APP IN JSON MODE ===");
            userRepo = new UserRepositoryImpl();
            vehicleRepo = new VehicleRepositoryImpl();
            rentalRepo = new RentalRepositoryImpl();
        }
        AuthService authService = new AuthService(userRepo);
        RentalService rentalService = new RentalService(rentalRepo, vehicleRepo);
        VehicleService vehicleService = new VehicleService(vehicleRepo, rentalService, validator);
        UserService userService = new UserService(userRepo, rentalService);

        UI ui = new UI(authService, rentalService, vehicleService, configService, userService, userRepo);
        ui.start();
    }
}