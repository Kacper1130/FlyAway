package FlyAway.flight.dao;

import FlyAway.flight.Flight;
import FlyAway.flight.FlightSpecification;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("sql")
public class SqlFlightAdapter implements FlightRepository {

    private final JpaFlightRepo jpaRepo;

    public SqlFlightAdapter(JpaFlightRepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Flight save(Flight flight) {
        return jpaRepo.save(flight);
    }

    @Override
    public Optional<Flight> findById(UUID id) {
        return jpaRepo.findById(id);
    }

    @Override
    public List<Flight> findAll() {
        return jpaRepo.findAll();
    }

    @Override
    public Page<Flight> findFutureFlights(Pageable pageable, LocalDateTime now) {
        return jpaRepo.findByDepartureDateAfter(pageable, now);
    }

    @Override
    public Page<Flight> findByFilters(Map<String, Object> filters, Pageable pageable) {
        // --- PRZENIESIONA LOGIKA ---
        // To tutaj konwertujemy mapę na specyfikację JPA.
        // Dzięki temu Serwis nie musi wiedzieć o istnieniu "Specification".
        Specification<Flight> spec = FlightSpecification.filterFlights(filters);

        return jpaRepo.findAll(spec, pageable);
    }

    @Override
    public List<Flight> saveAll(List<Flight> flights) {
        return jpaRepo.saveAll(flights);
    }
}
