package FlyAway.flight.aircraft.dao;

import FlyAway.flight.aircraft.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaAircraftRepo extends JpaRepository<Aircraft, UUID> {
    boolean existsByRegistration(String registration);
}
