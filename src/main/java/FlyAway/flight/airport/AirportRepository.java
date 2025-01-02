package FlyAway.flight.airport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AirportRepository extends JpaRepository<Airport, UUID> {

    Optional<Airport> findAirportByIATACode(String IATACode);
    @Query("SELECT a FROM Airport a ORDER BY a.country.name DESC")
    List<Airport> findAll();

    List<Airport> findAllByEnabledTrue();

}
