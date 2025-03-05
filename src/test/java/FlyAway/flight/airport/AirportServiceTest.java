package FlyAway.flight.airport;

import FlyAway.exception.AirportDoesNotExistException;
import FlyAway.exception.CountryDoesNotExistException;
import FlyAway.flight.airport.dto.AirportDto;
import FlyAway.flight.airport.dto.CreateAirportDto;
import FlyAway.flight.country.Country;
import FlyAway.flight.country.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportServiceTest {

    private static final Country USA_COUNTRY = Country.builder()
            .id(1)
            .name("United States")
            .enabled(true)
            .build();

    private static final UUID JFK_ID;
    private static final UUID LAX_ID;

    private static final Airport JFK_AIRPORT = Airport.builder()
            .id(null)
            .name("John F. Kennedy International Airport")
            .IATACode("JFK")
            .city("New York")
            .enabled(true)
            .country(USA_COUNTRY)
            .build();

    private static final Airport LAX_AIRPORT = Airport.builder()
            .id(null)
            .name("Los Angeles International Airport")
            .IATACode("LAX")
            .city("Los Angeles")
            .enabled(true)
            .country(USA_COUNTRY)
            .build();

    static {
        JFK_ID = UUID.randomUUID();
        LAX_ID = UUID.randomUUID();

        JFK_AIRPORT.setId(JFK_ID);
        LAX_AIRPORT.setId(LAX_ID);
    }

    @Mock
    AirportRepository airportRepository;

    @Mock
    CountryRepository countryRepository;

    @InjectMocks
    AirportService airportService;

    @Test
    void getAllAirports_WhenAirportsExists_ShouldReturnAllAirports() {
        when(airportRepository.findAll()).thenReturn(List.of(JFK_AIRPORT, LAX_AIRPORT));

        List<AirportDto> airports = airportService.getAllAirports();

        assertNotNull(airports);
        assertEquals(2, airports.size());

        assertEquals(JFK_AIRPORT.getId(), airports.get(0).id());
        assertEquals(JFK_AIRPORT.getName(), airports.get(0).name());
        assertEquals(JFK_AIRPORT.getIATACode(), airports.get(0).IATACode());
        assertEquals(JFK_AIRPORT.getCity(), airports.get(0).city());
        assertEquals(JFK_AIRPORT.isEnabled(), airports.get(0).enabled());
        assertEquals(JFK_AIRPORT.getCountry().getId(), airports.get(0).country().id());
        assertEquals(JFK_AIRPORT.getCountry().getName(), airports.get(0).country().name());
        assertEquals(JFK_AIRPORT.getCountry().isEnabled(), airports.get(0).country().enabled());

        assertEquals(LAX_AIRPORT.getId(), airports.get(1).id());
        assertEquals(LAX_AIRPORT.getName(), airports.get(1).name());
        assertEquals(LAX_AIRPORT.getIATACode(), airports.get(1).IATACode());
        assertEquals(LAX_AIRPORT.getCity(), airports.get(1).city());
        assertEquals(LAX_AIRPORT.isEnabled(), airports.get(1).enabled());
        assertEquals(LAX_AIRPORT.getCountry().getId(), airports.get(1).country().id());
        assertEquals(LAX_AIRPORT.getCountry().getName(), airports.get(1).country().name());
        assertEquals(LAX_AIRPORT.getCountry().isEnabled(), airports.get(1).country().enabled());

        verify(airportRepository, times(1)).findAll();
    }

    @Test
    void getAllAirports_WhenNoAirportExist_ShouldReturnAllAirports() {
        when(airportRepository.findAll()).thenReturn(List.of());

        List<AirportDto> airports = airportService.getAllAirports();

        assertNotNull(airports);
        assertEquals(0, airports.size());
        verify(airportRepository, times(1)).findAll();
    }

    @Test
    void createAirport_WhenCountryExists_ShouldCreateAndSaveAirport() {
        CreateAirportDto createAirportDto = new CreateAirportDto(
                JFK_AIRPORT.getName(),
                JFK_AIRPORT.getIATACode(),
                JFK_AIRPORT.getCity(),
                USA_COUNTRY.getName(),
                true
        );

        when(countryRepository.findByName(USA_COUNTRY.getName())).thenReturn(Optional.of(USA_COUNTRY));

        Airport savedAirport = airportService.createAirport(createAirportDto);

        assertNotNull(savedAirport);
        assertEquals(JFK_AIRPORT.getName(), savedAirport.getName());
        assertEquals(JFK_AIRPORT.getIATACode(), savedAirport.getIATACode());
        assertEquals(JFK_AIRPORT.getCity(), savedAirport.getCity());
        assertEquals(JFK_AIRPORT.getCountry().getName(), savedAirport.getCountry().getName());
        assertTrue(savedAirport.isEnabled());
        verify(airportRepository, times(1)).save(savedAirport);
    }

    @Test
    void createAirport_WhenCountryDoesNotExist_ShouldThrowException() {
        CreateAirportDto createAirportDto = new CreateAirportDto(
                JFK_AIRPORT.getName(),
                JFK_AIRPORT.getIATACode(),
                JFK_AIRPORT.getCity(),
                "Fake Country",
                true
        );

        when(countryRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        assertThrows(CountryDoesNotExistException.class, () -> airportService.createAirport(createAirportDto));
        verify(airportRepository, never()).save(any(Airport.class));
    }

    @Test
    void switchAirportStatus_WhenAirportExists_ShouldToggleStatus() {
        Airport airportToToggle = Airport.builder()
                .id(JFK_AIRPORT.getId())
                .name(JFK_AIRPORT.getName())
                .IATACode(JFK_AIRPORT.getIATACode())
                .city(JFK_AIRPORT.getCity())
                .enabled(true)
                .country(JFK_AIRPORT.getCountry())
                .build();

        when(airportRepository.findById(JFK_AIRPORT.getId())).thenReturn(Optional.of(airportToToggle));

        Airport updatedAirport = airportService.switchAirportStatus(JFK_AIRPORT.getId());

        assertFalse(updatedAirport.isEnabled());
        verify(airportRepository, times(1)).save(updatedAirport);
    }

    @Test
    void switchAirportStatus_WhenAirportDoesNotExist_ShouldThrowException() {
        UUID nonExistentId = UUID.randomUUID();
        when(airportRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(AirportDoesNotExistException.class, () -> airportService.switchAirportStatus(nonExistentId));
        verify(airportRepository, never()).save(any(Airport.class));
    }

    @Test
    void updateAirport_WhenAirportAndCountryExist_ShouldUpdateSuccessfully() {
        Airport existingAirport = Airport.builder()
                .id(JFK_AIRPORT.getId())
                .name(JFK_AIRPORT.getName())
                .IATACode(JFK_AIRPORT.getIATACode())
                .city(JFK_AIRPORT.getIATACode())
                .enabled(JFK_AIRPORT.isEnabled())
                .country(JFK_AIRPORT.getCountry())
                .build();

        Country poland = Country.builder()
                .id(2)
                .name("Poland")
                .enabled(true)
                .build();

        CreateAirportDto updateDto = new CreateAirportDto(
                "New airport name",
                "NEW",
                "Updated city",
                poland.getName(),
                false
        );

        when(airportRepository.findById(JFK_AIRPORT.getId())).thenReturn(Optional.of(existingAirport));
        when(countryRepository.findByName("Poland")).thenReturn(Optional.of(poland));

        Airport updatedAirport = airportService.updateAirport(JFK_AIRPORT.getId(), updateDto);

        assertEquals("New airport name", updatedAirport.getName());
        assertEquals("NEW", updatedAirport.getIATACode());
        assertEquals("Updated city", updatedAirport.getCity());
        assertEquals(poland, updatedAirport.getCountry());
        assertFalse(updatedAirport.isEnabled());
        verify(airportRepository, times(1)).save(updatedAirport);
    }

    @Test
    void updateAirport_WhenAirportDoesNotExist_ShouldThrowAirportDoesNotExistException() {
        CreateAirportDto updateDto = new CreateAirportDto(
                "New airport name",
                "NEW",
                "Updated city",
                "Poland",
                false
        );

        when(airportRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(AirportDoesNotExistException.class, () -> airportService.updateAirport(UUID.randomUUID(), updateDto));
        verify(airportRepository, never()).save(any());
    }

    @Test
    void updateAirport_WhenCountryDoesNotExist_ShouldThrowCountryDoesNotExistException() {
        Airport existingAirport = Airport.builder()
                .id(JFK_AIRPORT.getId())
                .name(JFK_AIRPORT.getName())
                .IATACode(JFK_AIRPORT.getIATACode())
                .city(JFK_AIRPORT.getCity())
                .enabled(JFK_AIRPORT.isEnabled())
                .country(JFK_AIRPORT.getCountry())
                .build();

        CreateAirportDto updateDto = new CreateAirportDto(
                "New airport name",
                "NEW",
                "Updated city",
                "NonExistentCountry",
                false
        );

        when(airportRepository.findById(JFK_AIRPORT.getId())).thenReturn(Optional.of(existingAirport));
        when(countryRepository.findByName("NonExistentCountry")).thenReturn(Optional.empty());

        assertThrows(CountryDoesNotExistException.class, () -> airportService.updateAirport(JFK_AIRPORT.getId(), updateDto));
        verify(airportRepository, never()).save(any());
    }

    @Test
    void getAllActiveAirports_WhenActiveAirportsActive_ShouldReturnEnabledAirports() {
        when(airportRepository.findAllByEnabledTrue()).thenReturn(Arrays.asList(JFK_AIRPORT, LAX_AIRPORT));

        List<AirportDto> activeAirports = airportService.getAllActiveAirports();

        assertNotNull(activeAirports);
        assertEquals(2, activeAirports.size());
        verify(airportRepository).findAllByEnabledTrue();
    }

    @Test
    void getAllActiveAirports_WhenNoActiveAirportExist_ShouldReturnEmptyList() {
        when(airportRepository.findAllByEnabledTrue()).thenReturn(List.of());

        List<AirportDto> activeAirports = airportService.getAllActiveAirports();

        assertNotNull(activeAirports);
        assertEquals(0, activeAirports.size());
        verify(airportRepository, times(1)).findAllByEnabledTrue();
    }

}