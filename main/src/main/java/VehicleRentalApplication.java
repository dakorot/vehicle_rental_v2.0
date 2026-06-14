import repository.VehicleCategoryConfigRepository;
import repository.impl.RentalRepositoryImpl;
import repository.impl.UserRepositoryImpl;
import repository.impl.VehicleCategoryConfigRepositoryImpl;
import repository.impl.VehicleRepositoryImpl;
import service.*;
import ui.UI;

public class VehicleRentalApplication {

    public static void main(String[] args) {
        VehicleCategoryConfigRepository configRepo = new VehicleCategoryConfigRepositoryImpl();
        VehicleCategoryConfigService configService = new VehicleCategoryConfigService(configRepo);
        VehicleValidator validator = new VehicleValidator(configService);

        VehicleRepositoryImpl vehicleRepo = new VehicleRepositoryImpl(); // Pamiętaj: wykładowca mówił, by użyć własnego!
        UserRepositoryImpl userRepo = new UserRepositoryImpl();
        RentalRepositoryImpl rentalRepo = new RentalRepositoryImpl();

        AuthService authService = new AuthService(userRepo);
        RentalService rentalService = new RentalService(rentalRepo, vehicleRepo);
        VehicleService vehicleService = new VehicleService(vehicleRepo, rentalService, validator);
        UserService userService = new UserService(userRepo, rentalService);

        UI ui = new UI(authService, rentalService, vehicleService, configService, userService, userRepo);
        ui.start();
    }
}