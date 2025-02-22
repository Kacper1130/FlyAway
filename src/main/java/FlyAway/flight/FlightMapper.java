package FlyAway.flight;

import FlyAway.flight.aircraft.AircraftMapper;
import FlyAway.flight.airport.AirportMapper;
import FlyAway.flight.dto.*;
import FlyAway.reservation.ReservationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ReservationMapper.class, AirportMapper.class, AircraftMapper.class})
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

    @Mapping(source = "departureAirportDto", target = "departureAirport")
    @Mapping(source = "arrivalAirportDto", target = "arrivalAirport")
    Flight createFlightDtoToFlight(CreateFlightDto createFlightDto);

    @Mapping(source = "departureAirport.city", target = "departureCity")
    @Mapping(source = "arrivalAirport.city", target = "arrivalCity")
    FlightBasicDto flightToFlightBasicDto(Flight flight);

    @Mapping(source = "departureAirport", target = "departureAirportDto")
    @Mapping(source = "arrivalAirport", target = "arrivalAirportDto")
    @Mapping(source = "aircraft", target = "aircraftDto")
    FlightDetailsClientDto flightToFlightDetailsClientDto(Flight flight);

    @Mapping(source = "departureAirport", target = "departureAirportDto")
    @Mapping(source = "arrivalAirport", target = "arrivalAirportDto")
    FlightDetailsEmployeeDto flightToFlightDetailsEmployeeDto(Flight flight);

    @Mapping(source = "departureAirport", target = "departureAirportDto")
    @Mapping(source = "arrivalAirport", target = "arrivalAirportDto")
    FlightSummaryEmployeeDto flightToFlightSummaryEmployeeDto(Flight flight);

}
