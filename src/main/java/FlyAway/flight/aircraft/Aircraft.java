package FlyAway.flight.aircraft;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aircrafts")
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank
    private String model;
    @NotNull
    private Integer productionYear;
    @NotNull
    private String registration;
    @NotNull
    private Integer totalSeats;
    @ElementCollection
    @MapKeyColumn(name = "class_type")
    @MapKeyEnumerated(EnumType.STRING)
    private Map<CabinClass, SeatClassRange> seatClassRanges;

}
