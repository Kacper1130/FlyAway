package FlyAway.flight.aircraft.dao;

import FlyAway.flight.aircraft.Aircraft;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AircraftRepository {

    Aircraft save(Aircraft aircraft);
    Optional<Aircraft> findById(UUID id);
    List<Aircraft> findAll();

    boolean existsByRegistration(String registration);

    void saveAll(List<Aircraft> aircrafts);
}
