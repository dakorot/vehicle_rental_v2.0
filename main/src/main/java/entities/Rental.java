package entities;

public class Rental {
    public String id;
    public String userLogin;
    public String vehicleId;

    public Rental(String id, String userLogin, String vehicleId) {
        this.id = id;
        this.userLogin = userLogin;
        this.vehicleId = vehicleId;
    }
}
