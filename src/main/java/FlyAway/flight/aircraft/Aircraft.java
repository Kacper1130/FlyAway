package FlyAway.flight.aircraft;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aircraft")
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotEmpty(message = "Model can not be empty")
    @NotBlank(message = "Model can not be blank")
    private String model;
    @NotNull(message = "Production year can not be null")
    private Integer productionYear;
    @NotEmpty(message = "Registration can not be empty")
    @NotBlank(message = "Registration can not be blank")
    private String registration;
    @NotNull(message = "Total seats can not be null")
    private Integer totalSeats;
    @ElementCollection
    @MapKeyColumn(name = "class_type")
    @MapKeyEnumerated(EnumType.STRING)
    @NotNull(message = "Seat class ranges can not be null")
    private Map<CabinClass, SeatClassRange> seatClassRanges;

}
