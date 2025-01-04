package FlyAway.flight;

import FlyAway.common.PageResponse;
import FlyAway.exception.CountryDoesNotExistException;
import FlyAway.exception.FlightDoesNotExistException;
import FlyAway.exception.MissingCabinClassPriceException;
import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.dto.FlightDetailsDto;
import FlyAway.flight.dto.FlightDto;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    private FlightMapper flightMapper = Mappers.getMapper(FlightMapper.class);

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public PageResponse<FlightDto> getFlights(int page, int size) {
        LOGGER.debug("Retrieving {} page with size {} from repository", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("departureDate").descending());
        Page<Flight> flights = flightRepository.findAll(pageable);
        List<FlightDto> flightsResponse = flights.stream().map(flightMapper::flightToFlightDto).toList();
        LOGGER.info("Retrieved {} flights from repository", flightsResponse);
        return new PageResponse<>(
                flightsResponse,
                flights.getNumber(),
                flights.getSize(),
                flights.getTotalElements(),
                flights.getTotalPages(),
                flights.isFirst(),
                flights.isLast()
        );
    }

    public FlightDto addFlight(FlightDto createFlightDto) {
        LOGGER.debug("Adding new flight");
        if (!createFlightDto.arrivalAirportDto().country().enabled() || !createFlightDto.departureAirportDto().country().enabled()) {
            throw new CountryDoesNotExistException();
        }
        for (var c : createFlightDto.aircraft().getSeatClassRanges().keySet()) {
            if (createFlightDto.cabinClassPrices().get(c) == null &&
                    createFlightDto.aircraft().getSeatClassRanges().get(c) != null) {
                throw new MissingCabinClassPriceException(c.toString());
            }
        }
        var classes = createFlightDto.cabinClassPrices().keySet();
        var newCabinClassPrices = new HashMap<>(createFlightDto.cabinClassPrices());
        for (var c : CabinClass.values()) {
            if (!classes.contains(c)) {
                newCabinClassPrices.put(c, null);
            }
        }
        var updatedFlightDto = new FlightDto(
                createFlightDto.departureAirportDto(),
                createFlightDto.arrivalAirportDto(),
                createFlightDto.departureDate(),
                createFlightDto.arrivalDate(),
                createFlightDto.aircraft(),
                newCabinClassPrices
        );
        Flight createdFlight = flightRepository.save(flightMapper.flightDtoToFlight(updatedFlightDto));
        LOGGER.info("Created flight with id {}", createdFlight.getId());
        return flightMapper.flightToFlightDto(createdFlight);
    }

    public List<FlightDetailsDto> getAllFlightsWithId() {
        LOGGER.debug("Retrieving all flights from repository");
        List<FlightDetailsDto> flights = flightRepository.findAll()
                .stream().map(flightMapper::flightToFlightDetailsDto).toList();
        LOGGER.info("Retrieved {} flights from repository", flights.size());
        return flights;
    }

    public FlightDetailsDto getFlightDetails(UUID id) {
        return flightRepository.findById(id).map(flightMapper::flightToFlightDetailsDto)
                .orElseThrow(FlightDoesNotExistException::new);
    }
}
