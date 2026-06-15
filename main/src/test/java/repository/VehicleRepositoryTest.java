package repository;

import com.dako.pl.entities.Vehicle;
import com.dako.pl.repository.IVehicleRepository;
import org.junit.jupiter.api.Test;
import com.dako.pl.repository.impl.VehicleRepositoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleRepositoryTest {

    @Test
    void getVehiclesShouldReturnDeepCopy() {
        IVehicleRepository repo = new VehicleRepositoryImpl();
        List<Vehicle> vehicles1 = repo.getVehicles();
        List<Vehicle> vehicles2  = repo.getVehicles();
        assertNotSame(vehicles1, vehicles2);
        assertNotSame(vehicles1.get(0), vehicles2.get(0));
    }

/*    @Test
    void addingToReturnedListShouldNotChangeRepository() {
        IVehicleRepository repo = new VehicleRepositoryImpl();
        List<Vehicle> vehicles = repo.getVehicles();
        int repoSizeBefore = repo.getVehicles().size();
        vehicles.add(new Car("100", "Test", "Test", 2026, 1, false));
        int repoSizeAfter = repo.getVehicles().size();
        assertEquals(repoSizeBefore, repoSizeAfter);
    }*/
}