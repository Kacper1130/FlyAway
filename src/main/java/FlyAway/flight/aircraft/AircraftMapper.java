package FlyAway.flight.aircraft;

import FlyAway.flight.aircraft.dto.AircraftDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AircraftMapper {

    AircraftMapper INSTANCE = Mappers.getMapper(AircraftMapper.class);

    AircraftDto aircraftToAircraftDto(Aircraft aircraft);

}
