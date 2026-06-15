package entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    public String login;

    public String password;

    @Enumerated(EnumType.STRING)
    public Role role;

    private User(Builder builder) {
        this.login = builder.login;
        this.password = builder.password;
        this.role = builder.role;
    }

    public User(User other) {
        this.login = other.login;
        this.password = other.password;
        this.role = other.role;
    }

    public static class Builder {
        private String login;
        private String password;
        private Role role;

        public Builder(String login, String password, Role role) {
            this.login = login;
            this.password = password;
            this.role = role;
        }


        public User build() {
            return new User(this);
        }
    }
}
