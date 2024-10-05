package FlyAway.flight.aircraft;

import FlyAway.exception.AircraftAlreadyExistsException;
import FlyAway.exception.InvalidSeatRangeException;
import FlyAway.exception.OverlappingSeatException;
import FlyAway.exception.TotalSeatsMismatchException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AircraftService {

    private final AircraftRepository aircraftRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(AircraftService.class);
    private static final List<CabinClass> ORDERED_CABIN_CLASSES =
            Arrays.asList(CabinClass.FIRST, CabinClass.BUSINESS, CabinClass.ECONOMY);

    public AircraftService(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    public List<Aircraft> getAllAircraft() {
        LOGGER.debug("Retrieving all aircraftList from repository");
        List<Aircraft> aircraftList = aircraftRepository.findAll();
        LOGGER.info("Retrieved {} aircraftList from repository", aircraftList.size());
        for (Aircraft a : aircraftList) {
            a.setSeatClassRanges(getOrderedSeatClassRanges(a.getSeatClassRanges()));
        }
        return aircraftList;
    }

    public Aircraft addAircraft(Aircraft aircraft) {
        LOGGER.debug("Adding new aircraft");
        aircraft.getSeatClassRanges().forEach((cabinClass, seatClassRange) -> {
            if (seatClassRange.startSeat() > seatClassRange.endSeat()) {
                LOGGER.error("Invalid seat class range for cabin class {}: start seat ({}) is greater than end seat ({})",
                        cabinClass, seatClassRange.startSeat(), seatClassRange.endSeat());
                throw new InvalidSeatRangeException(cabinClass);
            }
        });
        Integer totalSeatsInClasses = aircraft.getSeatClassRanges().values().stream()
                .mapToInt(seatClassRange -> seatClassRange.endSeat() - seatClassRange.startSeat() + 1)
                .sum();
        if (!totalSeatsInClasses.equals(aircraft.getTotalSeats())) {
            LOGGER.error("Total seats in seat classes ({}) does not match total seats on the aircraft ({})",
                    totalSeatsInClasses, aircraft.getTotalSeats());
            throw new TotalSeatsMismatchException();
        }
        Set<Integer> usedSeats = new HashSet<>();
        aircraft.getSeatClassRanges().forEach((cabinClass, seatClassRange) -> {
            for (int s = seatClassRange.startSeat(); s <= seatClassRange.endSeat(); s++) {
                if (!usedSeats.add(s)) {
                    LOGGER.error("Overlapping seat range detected for seat {}", s);
                    throw new OverlappingSeatException(s);
                }
            }
        });
        if (aircraftRepository.existsByRegistration(aircraft.getRegistration())) {
            LOGGER.error("Aircraft with registration {} already exists", aircraft.getRegistration());
            throw new AircraftAlreadyExistsException(aircraft.getRegistration());
        }
        aircraft.setModel(StringUtils.capitalize(aircraft.getModel()));
        Aircraft createdAircraft = aircraftRepository.save(aircraft);
        LOGGER.info("Created aircraft with id {}", createdAircraft.getId());
        return createdAircraft;
    }

    private Map<CabinClass, SeatClassRange> getOrderedSeatClassRanges(Map<CabinClass, SeatClassRange> originalRanges) {
        Map<CabinClass, SeatClassRange> orderedMap = new LinkedHashMap<>();
        for (CabinClass cabinClass : ORDERED_CABIN_CLASSES) {
            SeatClassRange range = originalRanges.get(cabinClass);
            orderedMap.put(cabinClass, range);
        }
        return orderedMap;
    }

}
