package FlyAway.flight.aircraft.dao;

import FlyAway.flight.aircraft.Aircraft;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("sql")
public class SqlAircraftAdapter implements AircraftRepository {

    private final JpaAircraftRepo jpaRepo;

    public SqlAircraftAdapter(JpaAircraftRepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Aircraft save(Aircraft aircraft) {
        return jpaRepo.save(aircraft);
    }

    @Override
    public Optional<Aircraft> findById(UUID id) {
        return jpaRepo.findById(id);
    }

    @Override
    public List<Aircraft> findAll() {
        return jpaRepo.findAll();
    }

    @Override
    public boolean existsByRegistration(String registration) {
        return jpaRepo.existsByRegistration(registration);
    }

    @Override
    public void saveAll(List<Aircraft> aircrafts) {
        jpaRepo.saveAll(aircrafts);
    }
}
