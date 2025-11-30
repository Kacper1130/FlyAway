package FlyAway.flight.dao;

import FlyAway.flight.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface FlightRepository {

    Flight save(Flight flight);

    Optional<Flight> findById(UUID id);

    // Dla Admina (lista wszystkich)
    List<Flight> findAll();

    // Dla Pasażera (Wyszukiwarka - daty przyszłe)
    Page<Flight> findFutureFlights(Pageable pageable, LocalDateTime now);

    // Dla Filtrowania (To jest ta trudna część!)
    // Przekazujemy czystą mapę filtrów. To Adapter zdecyduje, jak z niej zrobić zapytanie.
    Page<Flight> findByFilters(Map<String, Object> filters, Pageable pageable);

    List<Flight> saveAll(List<Flight> flight1);
}
