package FlyAway.reservation;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationSchedulerService {

    private final Logger LOGGER = LoggerFactory.getLogger(ReservationSchedulerService.class);
    private final ReservationRepository reservationRepository;

    public ReservationSchedulerService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @PostConstruct
    public void completeReservationsOnStartup() {
        completeReservations();
    }

    @Scheduled(cron = "0 0 * * * *")
    public void completeReservations() {
        List<Reservation> reservations = reservationRepository.findExpiredReservations(LocalDateTime.now());

        reservations.forEach(reservation -> reservation.setStatus(ReservationStatus.COMPLETED));
        reservationRepository.saveAll(reservations);

        LOGGER.info("Completed {} reservations.", reservations.size());
    }

}
