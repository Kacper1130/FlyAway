package FlyAway.flight.airport;

import FlyAway.WithMockAdmin;
import FlyAway.WithMockClient;
import FlyAway.exception.AirportDoesNotExistException;
import FlyAway.exception.CountryDoesNotExistException;
import FlyAway.flight.airport.dto.AirportDto;
import FlyAway.flight.airport.dto.CreateAirportDto;
import FlyAway.flight.country.Country;
import FlyAway.flight.country.dto.CountryDto;
import FlyAway.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AirportController.class)
class AirportControllerTest {

    private static final CountryDto USA_COUNTRY = new CountryDto(1, "United States", true);

    private static final UUID JFK_ID = UUID.randomUUID();
    private static final UUID LAX_ID = UUID.randomUUID();

    private static final AirportDto JFK_AIRPORT = new AirportDto(
            JFK_ID,
            "John F. Kennedy International Airport",
            "JFK",
            "New York",
            true,
            USA_COUNTRY);

    private static final AirportDto LAX_AIRPORT = new AirportDto(
            LAX_ID,
            "Los Angeles International Airport",
            "LAX",
            "Los Angeles",
            true,
            USA_COUNTRY);

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AirportService airportService;

    @MockBean
    private JwtService jwtService;


    @Test
    @WithMockClient
    void getAllAirports_WhenAirportsExist_ShouldReturnAirportList() throws Exception {
        List<AirportDto> airports = List.of(JFK_AIRPORT, LAX_AIRPORT);

        when(airportService.getAllAirports()).thenReturn(airports);

        mockMvc.perform(get("/api/v1/airports"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].id").value(JFK_ID.toString()))
                .andExpect(jsonPath("$[0].name").value("John F. Kennedy International Airport"))
                .andExpect(jsonPath("$[0].IATACode").value("JFK"))
                .andExpect(jsonPath("$[0].city").value("New York"))
                .andExpect(jsonPath("$[0].enabled").value(true))
                .andExpect(jsonPath("$[0].country.id").value(1))
                .andExpect(jsonPath("$[0].country.name").value("United States"))
                .andExpect(jsonPath("$[0].country.enabled").value(true))

                .andExpect(jsonPath("$[1].id").value(LAX_ID.toString()))
                .andExpect(jsonPath("$[1].name").value("Los Angeles International Airport"))
                .andExpect(jsonPath("$[1].IATACode").value("LAX"))
                .andExpect(jsonPath("$[1].city").value("Los Angeles"))
                .andExpect(jsonPath("$[1].enabled").value(true))
                .andExpect(jsonPath("$[1].country.id").value(1))
                .andExpect(jsonPath("$[1].country.name").value("United States"))
                .andExpect(jsonPath("$[1].country.enabled").value(true));

        verify(airportService, times(1)).getAllAirports();
    }

    @Test
    @WithMockClient
    void getAllAirports_WhenNoAirportsExist_ShouldReturnEmptyList() throws Exception {
        when(airportService.getAllAirports()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/airports"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(airportService, times(1)).getAllAirports();
    }

    @Test
    @WithMockAdmin
    void createAirport_WhenValidRequest_ShouldCreateAirport() throws Exception {
        CreateAirportDto createAirportDto = new CreateAirportDto(
                JFK_AIRPORT.name(),
                JFK_AIRPORT.IATACode(),
                JFK_AIRPORT.city(),
                JFK_AIRPORT.country().name(),
                JFK_AIRPORT.enabled()
        );

        UUID airportId = UUID.randomUUID();
        Airport savedAirport = new Airport(
                airportId,
                "John F. Kennedy International Airport",
                "JFK",
                "New York",
                true,
                new Country(
                        USA_COUNTRY.id(),
                        USA_COUNTRY.name(),
                        USA_COUNTRY.enabled(),
                        null
                )
        );

        when(airportService.createAirport(createAirportDto)).thenReturn(savedAirport);

        mockMvc.perform(post("/api/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAirportDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.IATACode").value("JFK"))
                .andExpect(jsonPath("$.name").value("John F. Kennedy International Airport"))
                .andExpect(jsonPath("$.city").value("New York"))
                .andExpect(jsonPath("$.enabled").value(true));

        verify(airportService, times(1)).createAirport(any(CreateAirportDto.class));
    }

    @Test
    @WithMockAdmin
    void createAirport_WhenMissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        CreateAirportDto invalidAirportDto = new CreateAirportDto(
                "",
                null,
                "",
                null,
                false
        );

        mockMvc.perform(post("/api/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAirportDto))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verify(airportService, never()).createAirport(any(CreateAirportDto.class));
    }

    @Test
    @WithMockAdmin
    void createAirport_WhenCountryDoesNotExist_ShouldThrowCountryDoesNotExistException() throws Exception {
        CreateAirportDto createAirportDto = new CreateAirportDto(
                JFK_AIRPORT.name(),
                JFK_AIRPORT.IATACode(),
                JFK_AIRPORT.city(),
                "NonExistentCountry",
                JFK_AIRPORT.enabled()
        );

        when(airportService.createAirport(createAirportDto)).thenThrow(new CountryDoesNotExistException());

        mockMvc.perform(post("/api/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAirportDto))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Country with given id does not exist"));

        verify(airportService, times(1)).createAirport(any(CreateAirportDto.class));
    }

    @Test
    @WithMockAdmin
    void switchAirportStatus_WhenAirportExists_ShouldToggleStatus() throws Exception {
        UUID airportId = UUID.randomUUID();
        Airport updatedAirport = new Airport(
                JFK_AIRPORT.id(),
                JFK_AIRPORT.name(),
                JFK_AIRPORT.IATACode(),
                JFK_AIRPORT.city(),
                !JFK_AIRPORT.enabled(),
                new Country(
                        USA_COUNTRY.id(),
                        USA_COUNTRY.name(),
                        USA_COUNTRY.enabled(),
                        null
                )
        );

        when(airportService.switchAirportStatus(JFK_ID)).thenReturn(updatedAirport);

        mockMvc.perform(patch("/api/v1/airports/{id}", JFK_ID)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(JFK_ID.toString()))
                .andExpect(jsonPath("$.enabled").value(!JFK_AIRPORT.enabled()));

        verify(airportService, times(1)).switchAirportStatus(JFK_ID);
    }

    @Test
    @WithMockAdmin
    void switchAirportStatus_WhenAirportDoesNotExist_ShouldThrowException() throws Exception {
        when(airportService.switchAirportStatus(JFK_ID)).thenThrow(new CountryDoesNotExistException());

        mockMvc.perform(patch("/api/v1/airports/{id}", JFK_ID)
                .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Country with given id does not exist"));

        verify(airportService, times(1)).switchAirportStatus(JFK_ID);
    }

    @Test
    @WithMockAdmin
    void updateAirport_WhenAirportAndCountryExist_ShouldUpdateSuccessfully() throws Exception {
        CreateAirportDto updateDto = new CreateAirportDto(
                JFK_AIRPORT.name(),
                JFK_AIRPORT.IATACode(),
                JFK_AIRPORT.city(),
                JFK_AIRPORT.country().name(),
                JFK_AIRPORT.enabled()
        );

        Airport updatedAirport = new Airport(
                JFK_ID,
                "Warsaw Chopin Airport",
                "WAW",
                "Warsaw",
                true,
                new Country(
                        2,
                        "Poland",
                        true,
                        null
                )

        );

        when(airportService.updateAirport(JFK_ID, updateDto))
                .thenReturn(updatedAirport);

        mockMvc.perform(put("/api/v1/airports/{id}", JFK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedAirport.getId().toString()))
                .andExpect(jsonPath("$.name").value("Warsaw Chopin Airport"))
                .andExpect(jsonPath("$.IATACode").value("WAW"))
                .andExpect(jsonPath("$.city").value("Warsaw"))
                .andExpect(jsonPath("$.enabled").value(true));

        verify(airportService, times(1)).updateAirport(eq(JFK_ID), any(CreateAirportDto.class));
    }

    @Test
    @WithMockAdmin
    void updateAirport_WhenAirportDoesNotExist_ShouldThrowAirportDoesNotExistException() throws Exception {
        CreateAirportDto updateDto = new CreateAirportDto(
                JFK_AIRPORT.name(),
                JFK_AIRPORT.IATACode(),
                JFK_AIRPORT.city(),
                JFK_AIRPORT.country().name(),
                JFK_AIRPORT.enabled()
        );

        when(airportService.updateAirport(JFK_ID, updateDto)).thenThrow(new AirportDoesNotExistException());

        mockMvc.perform(put("/api/v1/airports/{id}", JFK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Airport with given id does not exist"));

        verify(airportService, times(1)).updateAirport(JFK_ID, updateDto);

    }

    @Test
    @WithMockAdmin
    void updateAirport_WhenCountryDoesNotExist_ShouldThrowCountryDoesNotExistException() throws Exception {
        CreateAirportDto updateDto = new CreateAirportDto(
                JFK_AIRPORT.name(),
                JFK_AIRPORT.IATACode(),
                JFK_AIRPORT.city(),
                JFK_AIRPORT.country().name(),
                JFK_AIRPORT.enabled()
        );

        when(airportService.updateAirport(JFK_ID, updateDto)).thenThrow(new CountryDoesNotExistException());

        mockMvc.perform(put("/api/v1/airports/{id}", JFK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Country with given id does not exist"));

        verify(airportService, times(1)).updateAirport(JFK_ID, updateDto);
    }

    @Test
    @WithMockClient
    void getAllEnabledAirports_WhenActiveAirportsExists_ShouldReturnOnlyActiveAirports() throws Exception {
        List<AirportDto> activeAirports = List.of(JFK_AIRPORT, LAX_AIRPORT);

        when(airportService.getAllActiveAirports()).thenReturn(activeAirports);

        mockMvc.perform(get("/api/v1/airports/enabled"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].id").value(JFK_ID.toString()))
                .andExpect(jsonPath("$[0].name").value("John F. Kennedy International Airport"))
                .andExpect(jsonPath("$[0].IATACode").value("JFK"))
                .andExpect(jsonPath("$[0].city").value("New York"))
                .andExpect(jsonPath("$[0].enabled").value(true))
                .andExpect(jsonPath("$[0].country.id").value(1))
                .andExpect(jsonPath("$[0].country.name").value("United States"))
                .andExpect(jsonPath("$[0].country.enabled").value(true))

                .andExpect(jsonPath("$[1].id").value(LAX_ID.toString()))
                .andExpect(jsonPath("$[1].name").value("Los Angeles International Airport"))
                .andExpect(jsonPath("$[1].IATACode").value("LAX"))
                .andExpect(jsonPath("$[1].city").value("Los Angeles"))
                .andExpect(jsonPath("$[1].enabled").value(true))
                .andExpect(jsonPath("$[1].country.id").value(1))
                .andExpect(jsonPath("$[1].country.name").value("United States"))
                .andExpect(jsonPath("$[1].country.enabled").value(true));

        verify(airportService, times(1)).getAllActiveAirports();
    }

    @Test
    @WithMockClient
    void getAllEnabledAirports_WhenNoActiveAirportsExist_ShouldReturnOnlyActiveAirports() throws Exception {
        when(airportService.getAllActiveAirports()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/airports/enabled"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(airportService, times(1)).getAllActiveAirports();
    }

}