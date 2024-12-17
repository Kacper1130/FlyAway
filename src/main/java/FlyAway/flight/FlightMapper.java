package FlyAway.flight;

import FlyAway.flight.dto.FlightDetailsDto;
import FlyAway.flight.dto.FlightDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FlightMapper {

    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    @Mapping(source = "departureAirport", target = "departureAirportDto")
    @Mapping(source = "arrivalAirport", target = "arrivalAirportDto")
    FlightDto flightToFlightDto(Flight flight);

    @Mapping(source = "departureAirportDto", target = "departureAirport")
    @Mapping(source = "arrivalAirportDto", target = "arrivalAirport")
    Flight flightDtoToFlight(FlightDto flightDto);

    @Mapping(source = "departureAirport", target = "departureAirportDto")
    @Mapping(source = "arrivalAirport", target = "arrivalAirportDto")
    FlightDetailsDto flightToFlightDetailsDto(Flight flight);

}
