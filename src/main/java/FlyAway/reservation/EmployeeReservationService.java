package FlyAway.reservation;

import FlyAway.exception.ReservationDoesNotExistException;
import FlyAway.reservation.dto.DisplayReservationDto;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper = Mappers.getMapper(ReservationMapper.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeReservationService.class);

    public EmployeeReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<DisplayReservationDto> getAll() {
        LOGGER.debug("Retrieving all reservations from repository");
        List<DisplayReservationDto> reservations = reservationRepository.findAll()
                .stream().map(reservationMapper::reservationToDisplayReservationDto)
                .toList();
        LOGGER.info("Retrieved {} reservations", reservations.size());
        return reservations;
    }

    public void cancelReservation(UUID id) {
        LOGGER.debug("Cancelling reservation with id {}", id);
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);
            LOGGER.info("Successfully cancelled reservation with id {}", id);
        } else {
            LOGGER.error("Reservation with id {} does not exist", id);
            throw new ReservationDoesNotExistException(id);
        }
    }

}
