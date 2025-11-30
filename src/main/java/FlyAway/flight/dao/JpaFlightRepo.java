package FlyAway.flight.dao;

import FlyAway.flight.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.UUID;

public interface JpaFlightRepo extends JpaRepository<Flight, UUID>, JpaSpecificationExecutor<Flight> {

    // Spring Data JPA sam wygeneruje zapytanie: WHERE departure_date > :now
    Page<Flight> findByDepartureDateAfter(Pageable pageable, LocalDateTime now);
}
