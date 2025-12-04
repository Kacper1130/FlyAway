package FlyAway.flight.country.dao;

import FlyAway.flight.country.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaCountryRepo extends JpaRepository<Country, Integer> { // ID to Integer

    Optional<Country> findByName(String name);
    boolean existsByNameAndEnabledTrue(String name);
    List<Country> findAllByEnabledTrue();

    // Twoje sortowanie: najpierw włączone, potem alfabetycznie
    @Query("SELECT c FROM Country c ORDER BY c.enabled DESC, c.name ASC")
    List<Country> findAllSorted();
    Optional<Country> findById(UUID id);
}
