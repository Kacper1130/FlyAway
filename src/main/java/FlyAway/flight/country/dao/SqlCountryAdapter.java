package FlyAway.flight.country.dao;

import FlyAway.flight.country.Country;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("sql")
public class SqlCountryAdapter implements CountryRepository {

    private final JpaCountryRepo jpaRepo;

    public SqlCountryAdapter(JpaCountryRepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Country save(Country country) {
        return jpaRepo.save(country);
    }

    @Override
    public Optional<Country> findByName(String name) {
        return jpaRepo.findByName(name);
    }

    @Override
    public boolean existsByNameAndEnabledTrue(String name) {
        return jpaRepo.existsByNameAndEnabledTrue(name);
    }

    @Override
    public List<Country> findAllByEnabledTrue() {
        return jpaRepo.findAllByEnabledTrue();
    }

    @Override
    public List<Country> findAllSorted() {
        return jpaRepo.findAllSorted();
    }

    @Override
    public Optional<Country> findById(Integer id) {
        return jpaRepo.findById(id);
    }
}
