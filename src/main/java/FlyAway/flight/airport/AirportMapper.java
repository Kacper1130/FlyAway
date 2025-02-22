package FlyAway.flight.airport;

import FlyAway.flight.airport.dto.AirportBasicDto;
import FlyAway.flight.airport.dto.AirportCityIataCodeDto;
import FlyAway.flight.airport.dto.AirportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AirportMapper {

    AirportMapper INSTANCE = Mappers.getMapper(AirportMapper.class);

    AirportDto airportToAirportDto(Airport airport);

    @Mapping(source = "country.name", target = "country")
    AirportBasicDto airportToAirportBasicDto(Airport airport);

    AirportCityIataCodeDto airporToAirportCityIataCodeDto(Airport airport);
}
