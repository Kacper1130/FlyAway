package FlyAway.flight.country.dao;

import FlyAway.flight.country.Country;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CountryRepository {

    Country save(Country country);
    Optional<Country> findByName(String name);
    boolean existsByNameAndEnabledTrue(String name);

    // Metoda dla dropdowna (tylko włączone)
    List<Country> findAllByEnabledTrue();

    // Metoda dla listy admina (zastępuje findAllWithoutAirports)
    // Zwracamy CAŁE obiekty Country, posortowane.
    List<Country> findAllSorted();

    Optional<Country> findById(UUID id);
}
