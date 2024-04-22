package FlyAway.reservation;

import FlyAway.reservation.dto.CreateReservationDto;
import FlyAway.reservation.dto.DisplayReservationDto;
import FlyAway.reservation.dto.ReservationDto;
import FlyAway.reservation.dto.ReservationWithoutUserDto;
import FlyAway.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper (uses = UserMapper.class)
public interface ReservationMapper {

    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "flight.id", target = "flightId")
    DisplayReservationDto reservationToDisplayReservationDto(Reservation reservation);

    ReservationDto reservationToReservationDto(Reservation reservation);

    ReservationDto createReservationDtoToReservationDto(CreateReservationDto createReservationDto);

    ReservationWithoutUserDto reservationToReservationWithoutUserDto(Reservation reservation);

}
