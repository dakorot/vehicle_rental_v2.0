package repository.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entities.Vehicle;
import repository.IVehicleRepository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.Map;

public class VehicleRepositoryJdbcImpl implements IVehicleRepository {
    private final String dbUrl;
    private final Gson gson = new Gson();

    public VehicleRepositoryJdbcImpl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }

    @Override
    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicle";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vehicles.add(mapResultSetToVehicle(rs));
            }
        } catch (SQLException e) {
            System.out.println("Database error (Vehicle getAll): " + e.getMessage());
        }
        return vehicles;
    }

    @Override
    public void add(Vehicle vehicle) {
        if (vehicle.getId() == null || vehicle.getId().equals("0")) {
            vehicle.setId(generateNextId());
        }

        String sql = "INSERT INTO vehicle (id, category, brand, model, year, price, attributes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET " +
                "category = EXCLUDED.category, brand = EXCLUDED.brand, model = EXCLUDED.model, " +
                "year = EXCLUDED.year, price = EXCLUDED.price, attributes = EXCLUDED.attributes;";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vehicle.getId());
            pstmt.setString(2, vehicle.getCategory());
            pstmt.setString(3, vehicle.getBrand());
            pstmt.setString(4, vehicle.getModel());
            pstmt.setInt(5, vehicle.getYear());
            pstmt.setDouble(6, vehicle.getPrice());

            String attributesJson = gson.toJson(vehicle.getAttributes());
            pstmt.setString(7, attributesJson);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Database error (Vehicle add): " + e.getMessage());
        }
    }

    @Override
    public void remove(String id) {
        String sql = "DELETE FROM vehicle WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database error (Vehicle remove): " + e.getMessage());
        }
    }

    @Override
    public Vehicle getVehicle(String id) {
        String sql = "SELECT * FROM vehicle WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToVehicle(rs);
            }
        } catch (SQLException e) {
            System.out.println("Błąd bazy danych (Vehicle get): " + e.getMessage());
        }
        return null;
    }

    @Override
    public void save() {}

    private Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        String attributesJson = rs.getString("attributes");

        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> attributes = gson.fromJson(attributesJson, mapType);

        return Vehicle.builder()
                .id(rs.getString("id"))
                .category(rs.getString("category"))
                .brand(rs.getString("brand"))
                .model(rs.getString("model"))
                .year(rs.getInt("year"))
                .price(rs.getDouble("price"))
                .attributes(attributes)
                .build();
    }

    private String generateNextId() {
        int maxId = 0;
        String sql = "SELECT id FROM vehicle";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                try {
                    int currentId = Integer.parseInt(rs.getString("id"));
                    if (currentId > maxId) {
                        maxId = currentId;
                    }
                } catch (NumberFormatException e) {
                }
            }
        } catch (SQLException e) {
            System.out.println("Error occurred during ID generation: " + e.getMessage());
        }

        return Integer.toString(maxId + 1);
    }
}
