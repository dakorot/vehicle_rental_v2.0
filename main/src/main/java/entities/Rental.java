package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "rental")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Rental {
    @Id
    public String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    public Vehicle vehicle;

    @Column(name = "rent_date")
    public String rentDate;

    @Column(name = "return_date")
    public String returnDate;

    public Rental(String id, User user, Vehicle vehicle) {
        this.id = id;
        this.user = user;
        this.vehicle = vehicle;
        this.rentDate = java.time.LocalDate.now().toString();
    }
}
