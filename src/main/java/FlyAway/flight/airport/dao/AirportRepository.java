package FlyAway.flight.airport.dao;

import FlyAway.flight.airport.Airport;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AirportRepository {

    Airport save(Airport airport);
    Optional<Airport> findById(UUID id);

    // Metody biznesowe
    Optional<Airport> findAirportByIATACode(String iataCode);

    // Zmieniona nazwa dla jasności (zamiast magicznego findAll)
    List<Airport> findAllSortedByCountry();

    List<Airport> findAllByEnabledTrue();

    void saveAll(List<Airport> airports);
}
