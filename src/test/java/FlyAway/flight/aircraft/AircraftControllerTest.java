package FlyAway.flight.aircraft;

import FlyAway.WithMockAdmin;
import FlyAway.exception.AircraftAlreadyExistsException;
import FlyAway.exception.InvalidSeatRangeException;
import FlyAway.exception.OverlappingSeatException;
import FlyAway.exception.TotalSeatsMismatchException;
import FlyAway.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AircraftController.class)
@WithMockAdmin
class AircraftControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AircraftService aircraftService;

    @MockBean
    private JwtService jwtService;

    @Test
    void getAllAircraft_WhenAircraftExist_ShouldReturnAircraftList() throws Exception {
        Map<CabinClass, SeatClassRange> seatRanges1 = new EnumMap<>(CabinClass.class);
        seatRanges1.put(CabinClass.FIRST, new SeatClassRange(1, 10));
        seatRanges1.put(CabinClass.BUSINESS, new SeatClassRange(11, 30));
        seatRanges1.put(CabinClass.ECONOMY, new SeatClassRange(31, 150));

        Aircraft aircraft1 = Aircraft.builder()
                .model("Boeing 737")
                .productionYear(2010)
                .registration("SP-AA1")
                .totalSeats(150)
                .seatClassRanges(seatRanges1)
                .build();

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

        List<Aircraft> aircraftList = Arrays.asList(aircraft1, aircraft2);
        when(aircraftService.getAllAircraft()).thenReturn(aircraftList);

        mockMvc.perform(get("/api/v1/aircraft"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].model").value("Boeing 737"))
                .andExpect(jsonPath("$[0].productionYear").value(2010))
                .andExpect(jsonPath("$[0].registration").value("SP-AA1"))
                .andExpect(jsonPath("$[0].totalSeats").value(150))
                .andExpect(jsonPath("$[0].seatClassRanges.FIRST.startSeat").value(1))
                .andExpect(jsonPath("$[0].seatClassRanges.FIRST.endSeat").value(10))
                .andExpect(jsonPath("$[0].seatClassRanges.BUSINESS.startSeat").value(11))
                .andExpect(jsonPath("$[0].seatClassRanges.BUSINESS.endSeat").value(30))
                .andExpect(jsonPath("$[0].seatClassRanges.ECONOMY.startSeat").value(31))
                .andExpect(jsonPath("$[0].seatClassRanges.ECONOMY.endSeat").value(150))

                .andExpect(jsonPath("$[1].model").value("Airbus A320"))
                .andExpect(jsonPath("$[1].productionYear").value(2020))
                .andExpect(jsonPath("$[1].registration").value("SP-AA2"))
                .andExpect(jsonPath("$[1].totalSeats").value(120))
                .andExpect(jsonPath("$[1].seatClassRanges.FIRST").doesNotExist())
                .andExpect(jsonPath("$[1].seatClassRanges.BUSINESS.startSeat").value(1))
                .andExpect(jsonPath("$[1].seatClassRanges.BUSINESS.endSeat").value(20))
                .andExpect(jsonPath("$[1].seatClassRanges.ECONOMY.startSeat").value(21))
                .andExpect(jsonPath("$[1].seatClassRanges.ECONOMY.endSeat").value(120));

        verify(aircraftService, times(1)).getAllAircraft();
    }

    @Test
    void getAllAircraft_WhenNoAircraftExist_ShouldReturnEmptyList() throws Exception {
        when(aircraftService.getAllAircraft()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/aircraft"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(aircraftService, times(1)).getAllAircraft();
    }

    @Test
    void createAircraft_WhenValidRequest_ShouldCreateAircraft() throws Exception {
        Map<CabinClass, SeatClassRange> seatRanges = new EnumMap<>(CabinClass.class);
        seatRanges.put(CabinClass.FIRST, new SeatClassRange(1, 10));
        seatRanges.put(CabinClass.BUSINESS, new SeatClassRange(11, 30));
        seatRanges.put(CabinClass.ECONOMY, new SeatClassRange(31, 150));

        Aircraft aircraftRequest = Aircraft.builder()
                .model("Boeing 787")
                .productionYear(2015)
                .registration("SP-BB1")
                .totalSeats(150)
                .seatClassRanges(seatRanges)
                .build();

        UUID aircraftId = UUID.randomUUID();
        Aircraft savedAircraft = Aircraft.builder()
                .id(aircraftId)
                .model("Boeing 787")
                .productionYear(2015)
                .registration("SP-BB1")
                .totalSeats(150)
                .seatClassRanges(seatRanges)
                .build();

        when(aircraftService.createAircraft(any(Aircraft.class))).thenReturn(savedAircraft);

        mockMvc.perform(post("/api/v1/aircraft")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aircraftRequest))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.model").value("Boeing 787"))
                .andExpect(jsonPath("$.productionYear").value(2015))
                .andExpect(jsonPath("$.registration").value("SP-BB1"))
                .andExpect(jsonPath("$.totalSeats").value(150))
                .andExpect(jsonPath("$.seatClassRanges.FIRST.startSeat").value(1))
                .andExpect(jsonPath("$.seatClassRanges.FIRST.endSeat").value(10))
                .andExpect(jsonPath("$.seatClassRanges.BUSINESS.startSeat").value(11))
                .andExpect(jsonPath("$.seatClassRanges.BUSINESS.endSeat").value(30))
                .andExpect(jsonPath("$.seatClassRanges.ECONOMY.startSeat").value(31))
                .andExpect(jsonPath("$.seatClassRanges.ECONOMY.endSeat").value(150));

        verify(aircraftService, times(1)).createAircraft(any(Aircraft.class));
    }

    @Test
    void createAircraft_WhenMissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        Aircraft invalidAircraft = Aircraft.builder()
                .model(null)
                .productionYear(2015)
                .registration("")
                .build();

        mockMvc.perform(post("/api/v1/aircraft")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAircraft))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verify(aircraftService, never()).createAircraft(any(Aircraft.class));
    }

    @Test
    void createAircraft_WhenStartSeatGreaterThanEndSeat_ShouldThrowInvalidSeatRangeException() throws Exception {
        Map<CabinClass, SeatClassRange> seatRanges = new EnumMap<>(CabinClass.class);
        seatRanges.put(CabinClass.FIRST, new SeatClassRange(10, 5));
        seatRanges.put(CabinClass.BUSINESS, new SeatClassRange(11, 30));
        seatRanges.put(CabinClass.ECONOMY, new SeatClassRange(31, 150));

        Aircraft aircraftRequest = Aircraft.builder()
                .model("Boeing 787")
                .productionYear(2015)
                .registration("SP-BB1")
                .totalSeats(150)
                .seatClassRanges(seatRanges)
                .build();

        when(aircraftService.createAircraft(any(Aircraft.class)))
                .thenThrow(new InvalidSeatRangeException());

        mockMvc.perform(post("/api/v1/aircraft")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aircraftRequest))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Invalid seat range"));

        verify(aircraftService, times(1)).createAircraft(any(Aircraft.class));
    }

    @Test
    void createAircraft_WhenTotalSeatsMismatched_ShouldThrowTotalSeatsMismatchException() throws Exception {
        Map<CabinClass, SeatClassRange> seatRanges = new EnumMap<>(CabinClass.class);
        seatRanges.put(CabinClass.FIRST, new SeatClassRange(1, 10));
        seatRanges.put(CabinClass.BUSINESS, new SeatClassRange(11, 30));
        seatRanges.put(CabinClass.ECONOMY, new SeatClassRange(31, 150));

        Aircraft aircraftRequest = Aircraft.builder()
                .model("Boeing 787")
                .productionYear(2015)
                .registration("SP-BB1")
                .totalSeats(120)
                .seatClassRanges(seatRanges)
                .build();

        when(aircraftService.createAircraft(any(Aircraft.class)))
                .thenThrow(new TotalSeatsMismatchException());

        mockMvc.perform(post("/api/v1/aircraft")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aircraftRequest))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Total seats in seat classes does not match total seats on the aircraft"));

        verify(aircraftService, times(1)).createAircraft(any(Aircraft.class));
    }

    @Test
    void createAircraft_WhenSeatsOverlap_ShouldThrowOverlappingSeatException() throws Exception {
        Map<CabinClass, SeatClassRange> seatRanges = new EnumMap<>(CabinClass.class);
        seatRanges.put(CabinClass.FIRST, new SeatClassRange(1, 15));
        seatRanges.put(CabinClass.BUSINESS, new SeatClassRange(11, 30));
        seatRanges.put(CabinClass.ECONOMY, new SeatClassRange(31, 150));

        Aircraft aircraftRequest = Aircraft.builder()
                .model("Boeing 787")
                .productionYear(2015)
                .registration("SP-BB1")
                .totalSeats(150)
                .seatClassRanges(seatRanges)
                .build();

        when(aircraftService.createAircraft(any(Aircraft.class)))
                .thenThrow(new OverlappingSeatException());

        mockMvc.perform(post("/api/v1/aircraft")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aircraftRequest))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Seat ranges overlap between cabin classes"));

        verify(aircraftService, times(1)).createAircraft(any(Aircraft.class));
    }

    @Test
    void createAircraft_WhenRegistrationExists_ShouldThrowAircraftAlreadyExistsException() throws Exception {
        Map<CabinClass, SeatClassRange> seatRanges = new EnumMap<>(CabinClass.class);
        seatRanges.put(CabinClass.FIRST, new SeatClassRange(1, 10));
        seatRanges.put(CabinClass.BUSINESS, new SeatClassRange(11, 30));
        seatRanges.put(CabinClass.ECONOMY, new SeatClassRange(31, 150));

        Aircraft aircraftRequest = Aircraft.builder()
                .model("Boeing 787")
                .productionYear(2015)
                .registration("SP-AA1")
                .totalSeats(150)
                .seatClassRanges(seatRanges)
                .build();

        when(aircraftService.createAircraft(any(Aircraft.class)))
                .thenThrow(new AircraftAlreadyExistsException("SP-AA1"));

        mockMvc.perform(post("/api/v1/aircraft")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aircraftRequest))
                        .with(csrf()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Aircraft with registration 'SP-AA1' already exists"));

        verify(aircraftService, times(1)).createAircraft(any(Aircraft.class));
    }
}