package FlyAway.flight.aircraft;

import FlyAway.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AircraftServiceTest {

    private static final Map<CabinClass, SeatClassRange> seatRanges1 = new EnumMap<>(CabinClass.class);
    private static final Aircraft AIRCRAFT1 = Aircraft.builder()
            .model("Boeing 737")
            .productionYear(2010)
            .registration("SP-AA1")
            .totalSeats(150)
            .seatClassRanges(null)
            .build();

    static {
        seatRanges1.put(CabinClass.FIRST, new SeatClassRange(1, 10));
        seatRanges1.put(CabinClass.BUSINESS, new SeatClassRange(11, 30));
        seatRanges1.put(CabinClass.ECONOMY, new SeatClassRange(31, 150));
        AIRCRAFT1.setSeatClassRanges(seatRanges1);
    }

    @Mock
    AircraftRepository aircraftRepository;

    @InjectMocks
    AircraftService aircraftService;

    @Test
    void getAllAircraft_WhenAircraftExist_ShouldReturnAircraftList() {
        Map<CabinClass, SeatClassRange> seatRanges2 = new EnumMap<>(CabinClass.class);
        seatRanges2.put(CabinClass.BUSINESS, new SeatClassRange(1, 20));
        seatRanges2.put(CabinClass.ECONOMY, new SeatClassRange(21, 120));

        Aircraft aircraft2 = Aircraft.builder()
                .model("Airbus A320")
                .productionYear(2020)
                .registration("SP-AA2")
                .totalSeats(120)
                .seatClassRanges(seatRanges2)
                .build();

        when(aircraftRepository.findAll()).thenReturn(Arrays.asList(AIRCRAFT1, aircraft2));

        List<Aircraft> result = aircraftService.getAllAircraft();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Boeing 737", result.get(0).getModel());
        assertEquals(2010, result.get(0).getProductionYear());
        assertEquals("SP-AA1", result.get(0).getRegistration());
        assertEquals(150, result.get(0).getTotalSeats());
        assertEquals(seatRanges1, result.get(0).getSeatClassRanges());

        assertEquals("Airbus A320", result.get(1).getModel());
        assertEquals(2020, result.get(1).getProductionYear());
        assertEquals("SP-AA2", result.get(1).getRegistration());
        assertEquals(120, result.get(1).getTotalSeats());
        assertEquals(seatRanges2, result.get(1).getSeatClassRanges());
    }

    @Test
    void getAllAircraft_WhenNoAircraftExist_ShouldReturnEmptyList() {
        when(aircraftRepository.findAll()).thenReturn(List.of());

        List<Aircraft> result = aircraftService.getAllAircraft();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(aircraftRepository, times(1)).findAll();
    }

    @Test
    void createAircraft_ValidAircraft_ShouldCreateAndReturnAircraft() {
        UUID id = UUID.randomUUID();
        Map<CabinClass, SeatClassRange> seatRanges = new EnumMap<>(CabinClass.class);
        seatRanges.put(CabinClass.FIRST, new SeatClassRange(1, 10));
        seatRanges.put(CabinClass.BUSINESS, new SeatClassRange(11, 30));
        seatRanges.put(CabinClass.ECONOMY, new SeatClassRange(31, 150));

        Aircraft savedAircraft = Aircraft.builder()
                .id(id)
                .model("Boeing 737")
                .productionYear(2010)
                .registration("SP-ABC")
                .totalSeats(150)
                .seatClassRanges(seatRanges)
                .build();

        when(aircraftRepository.existsByRegistration(AIRCRAFT1.getRegistration())).thenReturn(false);
        when(aircraftRepository.save(AIRCRAFT1)).thenReturn(savedAircraft);

        Aircraft createdAircraft = aircraftService.createAircraft(AIRCRAFT1);

        assertNotNull(createdAircraft);
        assertEquals("Boeing 737", createdAircraft.getModel());
        verify(aircraftRepository).save(AIRCRAFT1);
    }

    @Test
    void createAircraft_DuplicateRegistration_ShouldThrowException() {
        when(aircraftRepository.existsByRegistration(AIRCRAFT1.getRegistration())).thenReturn(true);

        assertThrows(AircraftAlreadyExistsException.class, () -> aircraftService.createAircraft(AIRCRAFT1));
        verify(aircraftRepository, never()).save(any(Aircraft.class));
    }

    @Test
    void createAircraft_InvalidSeatClassRange_ShouldThrowException() {
        Map<CabinClass, SeatClassRange> seatRanges = new EnumMap<>(CabinClass.class);
        seatRanges.put(CabinClass.FIRST, new SeatClassRange(10, 1));

        Aircraft aircraft = Aircraft.builder()
                .model("Boeing 737")
                .productionYear(2010)
                .registration("SP-ABC")
                .totalSeats(150)
                .seatClassRanges(seatRanges)
                .build();

        assertThrows(InvalidSeatRangeException.class, () -> aircraftService.createAircraft(aircraft));
        verify(aircraftRepository, never()).save(any(Aircraft.class));
    }

    @Test
    void createAircraft_MismatchedTotalSeats_ShouldThrowException() {
        Map<CabinClass, SeatClassRange> seatRanges = new EnumMap<>(CabinClass.class);
        seatRanges.put(CabinClass.FIRST, new SeatClassRange(1, 10));
        seatRanges.put(CabinClass.BUSINESS, new SeatClassRange(11, 30));
        seatRanges.put(CabinClass.ECONOMY, new SeatClassRange(31, 150));

        Aircraft aircraft = Aircraft.builder()
                .model("Boeing 737")
                .productionYear(2010)
                .registration("SP-ABC")
                .totalSeats(200)
                .seatClassRanges(seatRanges)
                .build();

        assertThrows(TotalSeatsMismatchException.class, () -> aircraftService.createAircraft(aircraft));
        verify(aircraftRepository, never()).save(any(Aircraft.class));
    }

    @Test
    void createAircraft_OverlappingSeats_ShouldThrowException() {
        Map<CabinClass, SeatClassRange> seatRanges = new EnumMap<>(CabinClass.class);
        seatRanges.put(CabinClass.FIRST, new SeatClassRange(1, 10));
        seatRanges.put(CabinClass.BUSINESS, new SeatClassRange(9, 28));

        Aircraft aircraft = Aircraft.builder()
                .model("Boeing 737")
                .productionYear(2010)
                .registration("SP-ABC")
                .totalSeats(30)
                .seatClassRanges(seatRanges)
                .build();

        assertThrows(OverlappingSeatException.class, () -> aircraftService.createAircraft(aircraft));
        verify(aircraftRepository, never()).save(any(Aircraft.class));
    }

    @Test
    void getCabinClassBySeatNumber_ValidSeatNumber_ShouldReturnCorrectCabinClass() {
        Map<CabinClass, SeatClassRange> seatRanges = new EnumMap<>(CabinClass.class);
        seatRanges.put(CabinClass.FIRST, new SeatClassRange(1, 10));
        seatRanges.put(CabinClass.BUSINESS, new SeatClassRange(11, 30));
        seatRanges.put(CabinClass.ECONOMY, new SeatClassRange(31, 150));

        Aircraft aircraft = Aircraft.builder()
                .model("Boeing 737")
                .productionYear(2010)
                .registration("SP-ABC")
                .totalSeats(150)
                .seatClassRanges(seatRanges)
                .build();

        assertEquals(CabinClass.FIRST, aircraftService.getCabinClassBySeatNumber(aircraft, 5));
        assertEquals(CabinClass.BUSINESS, aircraftService.getCabinClassBySeatNumber(aircraft, 20));
        assertEquals(CabinClass.ECONOMY, aircraftService.getCabinClassBySeatNumber(aircraft, 100));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 151, -50})
    void getCabinClassBySeatNumber_InvalidSeatNumber_ShouldThrowException(Integer seatNumber) {
        Map<CabinClass, SeatClassRange> seatRanges = new EnumMap<>(CabinClass.class);
        seatRanges.put(CabinClass.FIRST, new SeatClassRange(1, 10));
        seatRanges.put(CabinClass.BUSINESS, new SeatClassRange(11, 30));
        seatRanges.put(CabinClass.ECONOMY, new SeatClassRange(31, 150));

        Aircraft aircraft = Aircraft.builder()
                .model("Boeing 737")
                .productionYear(2010)
                .registration("SP-ABC")
                .totalSeats(150)
                .seatClassRanges(seatRanges)
                .build();

        assertThrows(SeatNumberDoesNotBelongToAnyCabinClassException.class,
                () -> aircraftService.getCabinClassBySeatNumber(aircraft, seatNumber));
    }

}