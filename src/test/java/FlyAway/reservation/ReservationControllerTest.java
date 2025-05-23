//package FlyAway.reservation;
//
//import FlyAway.client.dto.ClientDto;
//import FlyAway.exception.FlightDoesNotExistException;
//import FlyAway.exception.ReservationDoesNotExistException;
//import FlyAway.exception.UserDoesNotExistException;
//import FlyAway.flight.dto.FlightDto;
//import FlyAway.reservation.dto.CreateReservationDto;
//import FlyAway.reservation.dto.DisplayReservationDto;
//import FlyAway.reservation.dto.ReservationDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertInstanceOf;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ClientReservationController.class)
//@AutoConfigureMockMvc(addFilters = false)
//class ReservationControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @MockBean
//    ClientReservationService reservationService;
//
//    @Test
//    void testGetAll() throws Exception {
//        DisplayReservationDto displayReservationDto1 = new DisplayReservationDto(
//                UUID.randomUUID(),
//                LocalDateTime.of(2024, 7, 5, 19, 30),
//                200L,
//                15L,
//                false,
//                1L,
//                UUID.randomUUID()
//        );
//
//        DisplayReservationDto displayReservationDto2 = new DisplayReservationDto(
//                UUID.randomUUID(),
//                LocalDateTime.of(2024, 7, 5, 19, 50),
//                400L,
//                81L,
//                false,
//                2L,
//                UUID.randomUUID()
//        );
//
//        List<DisplayReservationDto> reservations = new ArrayList<>();
//        reservations.add(displayReservationDto1);
//        reservations.add(displayReservationDto2);
//
//        when(reservationService.getAll()).thenReturn(reservations);
//
//        mockMvc.perform(get("/api/v1/reservations"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].id").value(displayReservationDto1.id().toString()))
//                .andExpect(jsonPath("$[1].id").value(displayReservationDto2.id().toString()));
//
//        verify(reservationService, times(1)).getAll();
//    }
//
//    @Test
//    void testGetAllWhenNoneExists() throws Exception {
//
//        when(reservationService.getAll()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/api/v1/reservations"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$").isEmpty());
//
//    }
//
//    @Test
//    void testAddReservation() throws Exception {
//        // Przykładowe dane wejściowe
//        CreateReservationDto createReservationDto = new CreateReservationDto(
//                200L,
//                15L,
//                1L,
//                UUID.randomUUID()
//        );
//
//        ClientDto userDto = new ClientDto(
//                "user1",
//                "user1",
//                "user1@gmail.com",
//                "532235213",
//                LocalDate.of(2000, 5, 2)
//        );
//
//        FlightDto flightDto = new FlightDto(
//                "Cracow",
//                "Warsaw",
//                LocalDateTime.of(2024, 5, 5, 10, 15),
//                LocalDateTime.of(2024, 5, 5, 11, 15),
//                "Ryanair"
//        );
//
//        ReservationDto reservationDto = new ReservationDto(
//                LocalDateTime.of(2024, 5, 7, 16, 30),
//                200L,
//                15L,
//                false,
//                userDto,
//                flightDto
//        );
//
//        when(reservationService.createReservation(createReservationDto)).thenReturn(reservationDto);
//
//        mockMvc.perform(post("/api/v1/reservations/add")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createReservationDto))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.reservationDate").isNotEmpty())
//                .andExpect(jsonPath("$.price").isNotEmpty())
//                .andExpect(jsonPath("$.seatNumber").isNotEmpty())
//                .andExpect(jsonPath("$.cancelled").isNotEmpty())
//                .andExpect(jsonPath("$.userDto").isNotEmpty())
//                .andExpect(jsonPath("$.flightDto").isNotEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.seatNumber").value(reservationDto.seatNumber()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.userDto.email").value(userDto.email()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.flightDto.airline").value(flightDto.airline()));
//
//
//        verify(reservationService, times(1)).createReservation(createReservationDto);
//    }
//
//    @Test
//    void testAddReservationWhenUserDoesNotExist() throws Exception {
//
//        CreateReservationDto createReservationDto = new CreateReservationDto(
//                200L,
//                15L,
//                2L,
//                UUID.randomUUID()
//        );
//
//        when(reservationService.createReservation(createReservationDto)).thenThrow(new UserDoesNotExistException());
//
//        mockMvc.perform(post("/api/v1/reservations/add")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createReservationDto)))
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(result -> assertInstanceOf(UserDoesNotExistException.class, result.getResolvedException()));
//
//    }
//
//    @Test
//    void testAddReservationWhenFlightDoesNotExist() throws Exception {
//
//        CreateReservationDto createReservationDto = new CreateReservationDto(
//                200L,
//                15L,
//                1L,
//                UUID.randomUUID()
//        );
//
//        when(reservationService.createReservation(any(CreateReservationDto.class)))
//                .thenThrow(new FlightDoesNotExistException());
//
//        mockMvc.perform(post("/api/v1/reservations/add")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createReservationDto)))
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(result -> assertInstanceOf(FlightDoesNotExistException.class, result.getResolvedException()));
//
//    }
//
//    @Test
//    void testAddReservationWhenInvalidInput() throws Exception {
//
//        CreateReservationDto createReservationDto = new CreateReservationDto(
//                200L,
//                null,
//                2L,
//                UUID.randomUUID()
//        );
//
//        mockMvc.perform(post("/api/v1/reservations/add")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createReservationDto)))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()));
//
//    }
//
//    @Test
//    public void testCancelReservation() throws Exception {
//
//        UUID reservationId = UUID.randomUUID();
//
//        mockMvc.perform(delete("/api/v1/reservations/" + reservationId + "/cancel"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Cancelled reservation"));
//
//    }
//
//    @Test
//    public void testCancelReservationWhenReservationDoesNotExist() throws Exception {
//
//        UUID reservationId = UUID.randomUUID();
//
//        doThrow(new ReservationDoesNotExistException())
//                .when(reservationService).cancelReservation(reservationId);
//
//        mockMvc.perform(delete("/api/v1/reservations/" + reservationId + "/cancel"))
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(result -> assertInstanceOf(ReservationDoesNotExistException.class, result.getResolvedException()));
//
//    }
//
//}