package entities;

import java.util.List;

public interface IVehicleRepository {
    public void rentVehicle(Vehicle vehicle);
    public Vehicle returnVehicle(String id);
    public List<Vehicle> getVehicles();
    public void save();
    public void load();
}
