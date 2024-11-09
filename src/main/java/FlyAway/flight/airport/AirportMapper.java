package FlyAway.flight.airport;

import FlyAway.flight.airport.dto.AirportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AirportMapper {

    AirportMapper INSTANCE = Mappers.getMapper(AirportMapper.class);

    //CreateAirportDto airportToCreateAirportDto(Airport airport);

    //Airport createAirportDtoToAirport(CreateAirportDto createAirportDto);

    //AirportDto createAirportDtoToAirportDto(CreateAirportDto createAirportDto);

    @Mapping(source = "country.name", target = "country")
    AirportDto airportToAirportDto(Airport airport);
}
