package FlyAway.reservation.dao;

import FlyAway.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface JpaReservationRepo extends JpaRepository<Reservation, UUID> {

    @Query("SELECT r FROM Reservation r WHERE r.status = 'ACTIVE' AND r.flight.arrivalDate < :now")
    List<Reservation> findExpiredReservations(@Param("now") LocalDateTime now);

    // ZMIANA: r.user.id zamiast r.client.id
    @Query("SELECT r FROM Reservation r WHERE r.client.id = :userId AND r.status = 'ACTIVE'")
    List<Reservation> findActiveByUserId(@Param("userId") UUID userId);

    // ZMIANA: r.user.id zamiast r.client.id
    @Query("SELECT r FROM Reservation r WHERE r.client.id = :userId")
    List<Reservation> findAllByUserId(@Param("userId") UUID userId);
}
