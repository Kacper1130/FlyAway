package FlyAway.reservation.dao;

import FlyAway.reservation.Reservation;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("sql")
public class SqlReservationAdapter implements ReservationRepository {

    private final JpaReservationRepo jpaRepo;

    public SqlReservationAdapter(JpaReservationRepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Reservation save(Reservation reservation) {
        return jpaRepo.save(reservation);
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return jpaRepo.findById(id);
    }

    @Override
    public List<Reservation> findActiveByUserId(UUID userId) {
        return jpaRepo.findActiveByUserId(userId);
    }

    @Override
    public List<Reservation> findAllByUserId(UUID userId) {
        return jpaRepo.findAllByUserId(userId);
    }

    @Override
    public Page<Reservation> findAll(Pageable pageable) {
        return jpaRepo.findAll(pageable);
    }

    @Override
    public List<Reservation> findExpiredReservations(LocalDateTime now) {
        return jpaRepo.findExpiredReservations(now);
    }

    @Override
    public List<Reservation> saveAll(List<Reservation> reservations) {
        return jpaRepo.saveAll(reservations);
    }
}
