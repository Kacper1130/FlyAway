package FlyAway.flight.airport;

import FlyAway.flight.airport.dto.CreateAirportDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AirportMapper {

    AirportMapper INSTANCE = Mappers.getMapper(AirportMapper.class);

    CreateAirportDto airportToCreateAirportDto(Airport airport);

    Airport createAirportDtoToAirport(CreateAirportDto createAirportDto);

}
