package FlyAway.flight;

import FlyAway.flight.dto.FlightDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FlightController.class)
@AutoConfigureMockMvc(addFilters = false)
class FlightControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    FlightService flightService;


    @Test
    void testGetAll() throws Exception {

        FlightDto flightDto1 = new FlightDto(
                "Cracow",
                "Warsaw",
                LocalDateTime.of(2024, Month.APRIL, 30, 14, 30),
                LocalDateTime.of(2024, Month.APRIL, 30, 15, 30),
                "Ryanair"
        );

        FlightDto flightDto2 = new FlightDto(
                "Warsaw",
                "London",
                LocalDateTime.of(2024, Month.MAY, 5, 12, 30),
                LocalDateTime.of(2024, Month.MAY, 5, 15, 30),
                "Ryanair"
        );

        List<FlightDto> flights = new ArrayList<>();
        flights.add(flightDto1);
        flights.add(flightDto2);

        //given(flightService.getAll()).willReturn(flights);
        when(flightService.getAll()).thenReturn(flights);

        mockMvc.perform(get("/api/v1/flights")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].departureCity").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].arrivalCity").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].departureDate").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].arrivalDate").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].airline").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].departureCity").value(flightDto1.departureCity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].arrivalDate")
                        .value(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(flightDto2.arrivalDate())));

        Mockito.verify(flightService, times(1)).getAll();
    }

    @Test
    void testGetAllWhenNoneExists() throws Exception {

        when(flightService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/flights")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

    }

    @Test
    void testAdd() throws Exception {

        FlightDto flightDto = new FlightDto(
                "Cracow",
                "Warsaw",
                LocalDateTime.of(2024, 5, 5, 10, 15),
                LocalDateTime.of(2024, 5, 5, 11, 15),
                "Ryanair"
        );

        when(flightService.addFlight(flightDto)).thenReturn(flightDto);

        mockMvc.perform(post("/api/v1/flights/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flightDto))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.departureCity").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.arrivalCity").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.departureDate").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.arrivalDate").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.airline").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.airline").value("Ryanair"));


    }

    @Test
    void testAddWhenInvalidFlight() throws Exception {

        FlightDto flightDto = new FlightDto(
                "Cracow",
                "",
                LocalDateTime.of(2024, 5, 5, 10, 15),
                LocalDateTime.of(2024, 5, 5, 11, 15),
                "Ryanair"
        );

        mockMvc.perform(post("/api/v1/flights/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flightDto))
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

}