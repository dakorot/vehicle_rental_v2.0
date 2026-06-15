import repository.*;
import repository.impl.*;
import service.*;
import service.impl.*;
import ui.UI;

public class VehicleRentalApplication {

    public static void main(String[] args) {
        VehicleCategoryConfigRepository configRepo = new VehicleCategoryConfigRepositoryImpl();
        VehicleCategoryConfigServiceImpl configService = new VehicleCategoryConfigServiceImpl(configRepo);
        VehicleValidator validator = new VehicleValidator(configService);

        String mode = "json";
        if (args.length > 0) {
            mode = args[0].toLowerCase();
        }

        IUserRepository userRepo;
        IVehicleRepository vehicleRepo;
        IRentalRepository rentalRepo;

        if (mode.equals("hibernate")) {
            System.out.println("=== RUNNING THE APP IN HIBERNATE MODE ===");
            userRepo = new UserRepositoryHibernate();
            vehicleRepo = new VehicleRepositoryHibernate();
            rentalRepo = new RentalRepositoryHibernate();
        } else if (mode.equals("jdbc")) {
            System.out.println("=== RUNNING THE APP IN JDBC MODE ===");
            String dbUrl = System.getenv("DB_URL");
            userRepo = new UserRepositoryJdbcImpl(dbUrl);
            vehicleRepo = new VehicleRepositoryJdbcImpl(dbUrl);
            rentalRepo = new RentalRepositoryJdbcImpl(dbUrl, userRepo, vehicleRepo);
        } else {
            System.out.println("=== RUNNING THE APP IN JSON MODE ===");
            userRepo = new UserRepositoryImpl();
            vehicleRepo = new VehicleRepositoryImpl();
            rentalRepo = new RentalRepositoryImpl();
        }
        AuthServiceImpl authServiceImpl = new AuthServiceImpl(userRepo);
        RentalServiceImpl rentalServiceImpl = new RentalServiceImpl(rentalRepo, vehicleRepo);
        VehicleServiceImpl vehicleServiceImpl = new VehicleServiceImpl(vehicleRepo, rentalServiceImpl, validator);
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepo, rentalServiceImpl);

        UI ui = new UI(authServiceImpl, rentalServiceImpl, vehicleServiceImpl, configService, userServiceImpl, userRepo);
        ui.start();
    }
}