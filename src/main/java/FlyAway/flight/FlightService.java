package FlyAway.flight;

import FlyAway.common.PageResponse;
import FlyAway.exception.CountryDoesNotExistException;
import FlyAway.exception.FlightDoesNotExistException;
import FlyAway.exception.MissingCabinClassPriceException;
import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.dto.AvailableSeatsDto;
import FlyAway.flight.dto.CreateFlightDto;
import FlyAway.flight.dto.FlightDetailsDto;
import FlyAway.flight.dto.FlightDto;
import FlyAway.reservation.ReservationStatus;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    private final FlightMapper flightMapper = Mappers.getMapper(FlightMapper.class);

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);
    private static final Set<String> RESERVED_PARAMS = Set.of("page", "size", "sort");


    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public PageResponse<FlightDto> getFlights(int page, int size) {
        LOGGER.debug("Retrieving {} page with size {} from repository", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("departureDate").ascending());
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

    public PageResponse<FlightDto> getFlightsByFilter(Map<String, Object> filters, int page, int size) {
        Map<String, Object> validFilters = cleanFilters(filters);
        LOGGER.info("Valid filters {}", validFilters);
        Specification<Flight> spec = FlightSpecification.filterFlights(validFilters);
        Pageable pageable = PageRequest.of(page, size, Sort.by("departureDate").ascending());
        Page<Flight> flights = flightRepository.findAll(spec, pageable);
        List<FlightDto> flightsResponse = flights.stream().map(flightMapper::flightToFlightDto).toList();
        LOGGER.info("Retrieved {} filtered flights from repository", flightsResponse);
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

    private Map<String, Object> cleanFilters(Map<String, Object> params) {
        return params.entrySet().stream()
                .filter(entry -> !RESERVED_PARAMS.contains(entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    public FlightDto addFlight(CreateFlightDto createFlightDto) {
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
        var updatedFlightDto = new CreateFlightDto(
                createFlightDto.departureAirportDto(),
                createFlightDto.arrivalAirportDto(),
                createFlightDto.departureDate(),
                createFlightDto.arrivalDate(),
                createFlightDto.aircraft(),
                newCabinClassPrices
        );
        Flight createdFlight = flightRepository.save(flightMapper.createFlightDtoToFlight(updatedFlightDto));
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
        LOGGER.info("Retrieving details for flight {}", id);
        Flight flight = flightRepository.findById(id)
                .orElseThrow(FlightDoesNotExistException::new);
        flight.getReservations().forEach(reservation -> System.out.println(reservation.getStatus()));
        LOGGER.info("Retrieved flight {}", flight);
        return flightMapper.flightToFlightDetailsDto(flight);
    }

    public AvailableSeatsDto getAvailableSeats(UUID id, String cabinClass) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(FlightDoesNotExistException::new);

        CabinClass cabinClassEnum = CabinClass.valueOf(cabinClass);
        var seatRange = flight.getAircraft().getSeatClassRanges().get(cabinClassEnum);

        Map<Integer, Boolean> availableSeats = new HashMap<>();
        for (int i = seatRange.startSeat(); i <= seatRange.endSeat(); i++) {
            availableSeats.put(i, true);
        }

        flight.getReservations().stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.ACTIVE && availableSeats.containsKey(reservation.getSeatNumber()))
                .forEach(reservation -> availableSeats.put(reservation.getSeatNumber(), false));

        return new AvailableSeatsDto(id, cabinClassEnum, availableSeats);
    }

}
