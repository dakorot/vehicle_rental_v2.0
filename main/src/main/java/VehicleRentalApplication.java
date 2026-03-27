import entities.Vehicle;
import entities.VehicleRepositoryImpl;

import java.util.List;

public class VehicleRentalApplication {
    public static void main(String[] args) {
        VehicleRepositoryImpl repo = new VehicleRepositoryImpl();

        repo.load();
        List<Vehicle> allVehicles = repo.getVehicles();
        for(Vehicle vehicle : allVehicles) {
            System.out.println(vehicle.toString());
        }
    }
}
