package FlyAway.reservation;

import FlyAway.common.PageResponse;
import FlyAway.exception.ReservationDoesNotExistException;
import FlyAway.reservation.dto.DisplayReservationDto;
import FlyAway.reservation.dto.ReservationDto;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public PageResponse<DisplayReservationDto> getReservations(int page, int size) {
        LOGGER.debug("Retrieving page {} with size {} from reservation repository", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("reservationDate").descending());
        Page<Reservation> reservations = reservationRepository.findAll(pageable);
        List<DisplayReservationDto> reservationsResponse = reservations
                .stream()
                .map(reservationMapper::reservationToDisplayReservationDto)
                .toList();
        LOGGER.info("Retrieved {} reservations", reservationsResponse.size());
        return new PageResponse<>(
                reservationsResponse,
                reservations.getNumber(),
                reservations.getSize(),
                reservations.getTotalElements(),
                reservations.getTotalPages(),
                reservations.isFirst(),
                reservations.isLast()
        );
    }

    public DisplayReservationDto getReservationSummary(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(ReservationDoesNotExistException::new);
        LOGGER.info("Found reservation with id {}", id);
        return reservationMapper.reservationToDisplayReservationDto(reservation);
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

    public ReservationDto getReservationDetails(UUID id) {
        LOGGER.debug("Retrieving reservation with id {}", id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(ReservationDoesNotExistException::new);
        LOGGER.info("Successfully retrieved reservation with id {}", id);
        return reservationMapper.reservationToReservationDto(reservation);
    }

}
