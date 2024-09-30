package FlyAway.flight.airport;

import FlyAway.flight.country.Country;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "airports")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotEmpty(message = "Name can not be empty")
    @NotBlank(message = "Name can not be blank")
    private String name;
    @NotEmpty(message = "IATA code can not be empty")
    @NotBlank(message = "IATA code can not be blank")
    @JsonProperty("IATACode")
    private String IATACode;
    @NotEmpty(message = "City can not be empty")
    @NotBlank(message = "City can not be blank")
    private String city;
    @ManyToOne
    private Country country;

}

