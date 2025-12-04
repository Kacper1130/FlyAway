package FlyAway.flight;

import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.airport.AirportDocument;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@Document(collection = "flights")
public class FlightDocument {

    @Id
    private UUID id;

    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;

    // ZAGNIEŻDŻENIE (Embedding) - Kluczowe dla wydajności!
    // Trzymamy kopię danych lotniska, żeby nie robić "JOIN" przy szukaniu.
    private AirportDocument departureAirport;
    private AirportDocument arrivalAirport;

    // RELACJA (Reference) - Samolot trzymamy luźno, po ID
    private UUID aircraftId;

    // Ceny mapują się automatycznie na JSON: { "ECONOMY": 100.00, ... }
    private Map<CabinClass, BigDecimal> cabinClassPrices;
}
