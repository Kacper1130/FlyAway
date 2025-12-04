package FlyAway.flight.country;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id; // Spring Data ID
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Builder
@Document(collection = "countries")
public class CountryDocument {

    @Id
    private UUID id; // Utrzymujemy Integer, bo tak masz w interfejsie

    @Indexed(unique = true)
    private String name;

    private boolean enabled;

    // UWAGA: Nie dodajemy tutaj listy List<AirportDocument>.
    // W Mongo trzymamy to w osobnej kolekcji.
}
