package FlyAway.flight.airport.dao;

import FlyAway.flight.airport.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaAirportRepo extends JpaRepository<Airport, UUID> {

    Optional<Airport> findAirportByIATACode(String iataCode);

    // Twoje sortowanie po nazwie kraju
    @Query("SELECT a FROM Airport a ORDER BY a.country.name DESC")
    List<Airport> findAllSortedByCountry();

    List<Airport> findAllByEnabledTrue();
}
