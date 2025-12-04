package FlyAway.reservation;

import FlyAway.flight.aircraft.CabinClass;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Document(collection = "reservations")
public class ReservationDocument {

    @Id
    private UUID id;

    private LocalDateTime reservationDate;
    private BigDecimal price;
    private Integer seatNumber;
    private CabinClass cabinClass;
    private ReservationStatus status;

    // RELACJE (Reference by ID)
    // W Mongo trzymamy tylko "wskaźniki" do innych dokumentów
    private UUID userId;
    private UUID flightId;

    // DENORMALIZACJA (Optimization)
    // Kopiujemy datę przylotu z Lotu, żeby szybko wyszukiwać wygasłe rezerwacje
    // bez konieczności robienia "Joinów" w zapytaniu.
    private LocalDateTime flightArrivalDate;
}
