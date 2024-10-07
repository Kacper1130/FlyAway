package FlyAway.flight.country;

import FlyAway.flight.airport.Airport;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "Country name can not be empty")
    @NotBlank(message = "Country can not be blank")
    @Column(unique = true)
    private String name;
    private boolean enabled;
    @OneToMany(mappedBy = "country", fetch = FetchType.EAGER)
    private List<Airport> airports;
}
