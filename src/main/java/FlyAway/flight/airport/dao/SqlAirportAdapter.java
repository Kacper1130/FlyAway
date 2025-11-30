package FlyAway.flight.airport.dao;

import FlyAway.flight.airport.Airport;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("sql")
public class SqlAirportAdapter implements AirportRepository {

    private final JpaAirportRepo jpaRepo;

    public SqlAirportAdapter(JpaAirportRepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Airport save(Airport airport) {
        return jpaRepo.save(airport);
    }

    @Override
    public Optional<Airport> findById(UUID id) {
        return jpaRepo.findById(id);
    }

    @Override
    public Optional<Airport> findAirportByIATACode(String iataCode) {
        return jpaRepo.findAirportByIATACode(iataCode);
    }

    @Override
    public List<Airport> findAllSortedByCountry() {
        return jpaRepo.findAllSortedByCountry();
    }

    @Override
    public List<Airport> findAllByEnabledTrue() {
        return jpaRepo.findAllByEnabledTrue();
    }

    @Override
    public void saveAll(List<Airport> airports) {
        jpaRepo.saveAll(airports);
    }
}
