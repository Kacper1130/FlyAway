package FlyAway.reservation;

import FlyAway.common.PageResponse;
import FlyAway.exception.ReservationDoesNotExistException;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.reservation.dto.ReservationSummaryEmployeeDto;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper = Mappers.getMapper(ReservationMapper.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeReservationService.class);

    public EmployeeReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public PageResponse<ReservationSummaryEmployeeDto> getReservations(int page, int size) {
        LOGGER.debug("Retrieving page {} with size {} from reservation repository", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("reservationDate").descending());
        Page<Reservation> reservations = reservationRepository.findAll(pageable);
        List<ReservationSummaryEmployeeDto> reservationsResponse = reservations
                .stream()
                .map(reservationMapper::reservationToReservationSummaryEmployeeDto)
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

    public ReservationSummaryEmployeeDto getReservationSummary(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(ReservationDoesNotExistException::new);
        LOGGER.info("Found reservation with id {}", id);
        return reservationMapper.reservationToReservationSummaryEmployeeDto(reservation);
    }

    public void cancelReservation(UUID id) {
        LOGGER.debug("Cancelling reservation with id {}", id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(ReservationDoesNotExistException::new);

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
        LOGGER.info("Successfully cancelled reservation with id {}", id);
    }

    public ReservationDto getReservationDetails(UUID id) {
        LOGGER.debug("Retrieving reservation with id {}", id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(ReservationDoesNotExistException::new);
        LOGGER.info("Successfully retrieved reservation with id {}", id);
        return reservationMapper.reservationToReservationDto(reservation);
    }

}
