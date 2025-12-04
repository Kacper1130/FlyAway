package FlyAway.flight.airport;

import FlyAway.flight.country.Country;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Builder
@Document(collection = "airports")
public class AirportDocument {

    @Id
    private UUID id;

    private String name;

    @Indexed(unique = true) // IATA musi być unikalne
    private String IATACode; // Zachowujemy Twoją nazwę pola (wielkie litery)

    private String city;
    private boolean enabled;

    // TO JEST KLUCZOWE:
    // Zapiszemy tu cały obiekt { id: 1, name: "Poland", ... }
    // Dzięki temu sortowanie po 'country.name' zadziała błyskawicznie.
    private Country country;
}
