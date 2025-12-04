package FlyAway.flight.aircraft;

import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.aircraft.SeatClassRange;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@Document(collection = "aircrafts")
public class AircraftDocument {

    @Id
    private UUID id;

    private String model;
    private Integer productionYear;

    @Indexed(unique = true) // Rejestracja musi być unikalna
    private String registration;

    private Integer totalSeats;

    // Mongo automatycznie zmapuje to na zagnieżdżony JSON.
    // Klucze mapy (Enum CabinClass) będą stringami.
    private Map<CabinClass, SeatClassRange> seatClassRanges;
}
