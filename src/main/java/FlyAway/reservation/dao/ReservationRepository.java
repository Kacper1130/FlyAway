package FlyAway.reservation.dao;

import FlyAway.reservation.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {

    // Podstawowe CRUD
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(UUID id);

    // Dla ClientReservationService
    List<Reservation> findActiveByUserId(Long userId);
    List<Reservation> findAllByUserId(Long userId);

    // Dla EmployeeReservationService (Paginacja)
    // Spring Data Mongo też obsługuje Pageable, więc możemy to zostawić w interfejsie
    Page<Reservation> findAll(Pageable pageable);

    // Opcjonalne (np. do czyszczenia wygasłych)
    List<Reservation> findExpiredReservations(LocalDateTime now);

    List<Reservation> saveAll(List<Reservation> reservations);
}
