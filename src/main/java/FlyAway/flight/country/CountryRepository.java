package FlyAway.flight.country;

import FlyAway.flight.country.dto.CountryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer> {

    List<Country> findAllByEnabledTrue();
    @Query("" +
            "SELECT new FlyAway.flight.country.dto.CountryDto(c.id, c.name, c.enabled) " +
            "FROM Country c " +
            "ORDER BY c.enabled DESC, c.name ASC" +
            "")
    List<CountryDto> findAllWithoutAirports();

    Optional<Country> findByName(String countryName);

}
