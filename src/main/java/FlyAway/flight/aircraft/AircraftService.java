package FlyAway.flight.aircraft;

import FlyAway.flight.airport.AirportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AircraftService {

    private final AircraftRepository aircraftRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(AirportService.class);
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
