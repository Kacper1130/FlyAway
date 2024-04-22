package FlyAway.reservation;

import FlyAway.flight.FlightMapper;
import FlyAway.reservation.dto.CreateReservationDto;
import FlyAway.reservation.dto.DisplayReservationDto;
import FlyAway.reservation.dto.ReservationDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReservationMapper {

    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    DisplayReservationDto reservationToDisplayReservationDto(Reservation reservation);

    ReservationDto reservationToReservationDto(Reservation reservation);

    ReservationDto createReservationDtoToReservationDto(CreateReservationDto createReservationDto);

}
