package FlyAway.reservation;

import FlyAway.flight.FlightMapper;
import FlyAway.reservation.dto.CreateReservationDto;
import FlyAway.reservation.dto.DisplayReservationDto;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.reservation.dto.ReservationWithoutUserDto;
import FlyAway.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper (uses = {UserMapper.class, FlightMapper.class})
public interface ReservationMapper {

    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "flight.id", target = "flightId")
    DisplayReservationDto reservationToDisplayReservationDto(Reservation reservation);

    @Mapping(source = "user", target = "userDto")
    @Mapping(source = "flight", target = "flightDto")
    ReservationDto reservationToReservationDto(Reservation reservation);

    @Mapping(source = "flight", target = "flightDto")
    ReservationWithoutUserDto reservationToReservationWithoutUserDto(Reservation reservation);

}
