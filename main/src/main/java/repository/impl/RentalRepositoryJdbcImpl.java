package repository.impl;

import entities.Rental;
import repository.IRentalRepository;
import repository.IUserRepository;
import repository.IVehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class RentalRepositoryJdbcImpl implements IRentalRepository {
    private final String dbUrl;
    private final IUserRepository userRepo;
    private final IVehicleRepository vehicleRepo;

    public RentalRepositoryJdbcImpl(String dbUrl, IUserRepository userRepo, IVehicleRepository vehicleRepo) {
        this.dbUrl = dbUrl;
        this.userRepo = userRepo;
        this.vehicleRepo = vehicleRepo;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }

    @Override
    public void add(Rental rental) {
        String sql = "INSERT INTO rental (id, vehicle_id, user_id, rent_date) " +
                "VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET " +
                "vehicle_id = EXCLUDED.vehicle_id, user_id = EXCLUDED.user_id;";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rental.id);
            pstmt.setString(2, rental.vehicle.getId());
            pstmt.setString(3, rental.user.getLogin());
            pstmt.setString(4, java.time.LocalDate.now().toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error (Rental add): " + e.getMessage());
        }
    }

    @Override
    public void remove(String rentalId) {
        String sql = "DELETE FROM rental WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rentalId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Database error (Rental remove): " + e.getMessage());
        }
    }

    @Override
    public List<Rental> getRentals() {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT id, user_id, vehicle_id FROM rental";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String userLogin = rs.getString("user_id");
                String vehicleId = rs.getString("vehicle_id");

                rentals.add(new Rental(id, userRepo.getUser(userLogin), vehicleRepo.getVehicle(vehicleId)));
            }
        } catch (SQLException e) {
            System.out.println("Database error (Rental getRentals): " + e.getMessage());
        }
        return rentals;
    }

    @Override
    public void save() {}

    @Override
    public void load() {}
}
