package FlyAway.flight.country;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Integer> {

    @Query("SELECT c FROM Country c ORDER BY c.enabled DESC, c.name ASC")
    List<Country> findAll();

}
