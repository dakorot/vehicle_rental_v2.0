package entities;

public class User {
    public String login;
    public String password;
    public Role role;
    public String rentedVehicleId;

    private User(Builder builder) {
        this.login = builder.login;
        this.password = builder.password;
        this.role = builder.role;
        this.rentedVehicleId = builder.rentedVehicleId;;
    }

    public User(User other) {
        this.login = other.login;
        this.password = other.password;
        this.role = other.role;
        this.rentedVehicleId = other.rentedVehicleId;
    }

    public static class Builder {
        private String login;
        private String password;
        private Role role;
        private String rentedVehicleId = null;

        public Builder(String login, String password, Role role) {
            this.login = login;
            this.password = password;
            this.role = role;
        }

        public Builder rentedVehicleId(String rentedVehicleId) {
            this.rentedVehicleId = rentedVehicleId;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
