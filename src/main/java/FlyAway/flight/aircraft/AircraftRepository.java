package FlyAway.flight.aircraft;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AircraftRepository extends JpaRepository<Aircraft, UUID> {

    Optional<Aircraft> findAircraftByRegistration(String registration);

}
