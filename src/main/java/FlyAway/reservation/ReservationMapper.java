package FlyAway.reservation;

import FlyAway.client.ClientMapper;
import FlyAway.flight.FlightMapper;
import FlyAway.reservation.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ClientMapper.class, FlightMapper.class})
public interface ReservationMapper {

    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "flight.id", target = "flightId")
    DisplayReservationDto reservationToDisplayReservationDto(Reservation reservation);

    @Mapping(source = "client", target = "clientDto")
    @Mapping(source = "flight", target = "flightDto")
    ReservationDto reservationToReservationDto(Reservation reservation);

    @Mapping(source = "flight", target = "flightDto")
    ReservationWithoutUserDto reservationToReservationWithoutUserDto(Reservation reservation);

    @Mapping(source = "client", target = "clientDto")
    ReservationWithoutFlightDto reservationToReservationWithoutFlightDto(Reservation reservation);

    @Mapping(source = "flight", target = "flightDto")
    ReservationSummaryClientDto reservationToReservationSummaryClientDto(Reservation reservation);

    @Mapping(source = "flight", target = "flightDto")
    ReservationDetailsClientDto reservationToReservationDetailsClientDto(Reservation reservation);

    @Mapping(source = "flight", target = "flightDto")
    @Mapping(source = "client", target = "clientDto")
    ReservationSummaryEmployeeDto reservationToReservationSummaryEmployeeDto(Reservation reservation);

}
