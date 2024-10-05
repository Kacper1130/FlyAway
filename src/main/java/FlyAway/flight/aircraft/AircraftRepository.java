package FlyAway.flight.aircraft;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AircraftRepository extends JpaRepository<Aircraft, UUID> {

    boolean existsByRegistration(String registration);
}
