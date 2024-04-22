package FlyAway.flight;

import FlyAway.flight.dto.FlightDto;
import FlyAway.reservation.dto.CreateReservationDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FlightMapper {

    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    FlightDto flightToFlightDto(Flight flight);

    Flight flightDtoToFlight(FlightDto flightDto);

}
