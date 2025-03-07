package FlyAway.flight.country;

import FlyAway.exception.CountryDoesNotExistException;
import FlyAway.flight.airport.Airport;
import FlyAway.flight.country.dto.CountryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    private static final Country USA = Country.builder()
            .id(1)
            .name("United States")
            .enabled(true)
            .build();

    private static final Country GERMANY = Country.builder()
            .id(2)
            .name("Germany")
            .enabled(true)
            .build();

    private static final Country FRANCE = Country.builder()
            .id(3)
            .name("France")
            .enabled(false)
            .build();

    private static final CountryDto USA_DTO = new CountryDto(
            USA.getId(),
            USA.getName(),
            USA.isEnabled()
    );

    private static final CountryDto GERMANY_DTO = new CountryDto(
            GERMANY.getId(),
            GERMANY.getName(),
            GERMANY.isEnabled()
    );

    private static final CountryDto FRANCE_DTO = new CountryDto(
            FRANCE.getId(),
            FRANCE.getName(),
            FRANCE.isEnabled()
    );

    @Mock
    CountryRepository countryRepository;

    @InjectMocks
    CountryService countryService;

    @Test
    void getAllCountries_WhenCountriesExist_ShouldReturnAllCountries() {
        when(countryRepository.findAllWithoutAirports()).thenReturn(
                Arrays.asList(USA_DTO, GERMANY_DTO, FRANCE_DTO));

        List<CountryDto> countries = countryService.getAllCountries();

        assertNotNull(countries);
        assertEquals(3, countries.size());
        assertEquals(USA_DTO.id(), countries.get(0).id());
        assertEquals(USA_DTO.name(), countries.get(0).name());
        assertEquals(USA_DTO.enabled(), countries.get(0).enabled());

        assertEquals(GERMANY_DTO.id(), countries.get(1).id());
        assertEquals(GERMANY_DTO.name(), countries.get(1).name());
        assertEquals(GERMANY_DTO.enabled(), countries.get(1).enabled());

        assertEquals(FRANCE_DTO.id(), countries.get(2).id());
        assertEquals(FRANCE_DTO.name(), countries.get(2).name());
        assertEquals(FRANCE_DTO.enabled(), countries.get(2).enabled());

        verify(countryRepository, times(1)).findAllWithoutAirports();
    }

    @Test
    void getAllCountries_WhenNoCountriesExist_ShouldReturnEmptyList() {
        when(countryRepository.findAllWithoutAirports()).thenReturn(List.of());

        List<CountryDto> countries = countryService.getAllCountries();

        assertNotNull(countries);
        assertEquals(0, countries.size());
        verify(countryRepository, times(1)).findAllWithoutAirports();
    }

    @Test
    void switchCountryStatus_WhenCountryExists_ShouldToggleStatus() {
        Airport airport1 = Airport.builder()
                .id(null)
                .name("JFK Airport")
                .IATACode("JFK")
                .city("New York")
                .enabled(true)
                .country(USA)
                .build();

        Airport airport2 = Airport.builder()
                .id(null)
                .name("LAX Airport")
                .IATACode("LAX")
                .city("Los Angeles")
                .enabled(true)
                .country(USA)
                .build();

        Country usaWithAirports = Country.builder()
                .id(USA.getId())
                .name(USA.getName())
                .enabled(USA.isEnabled())
                .airports(List.of(airport1, airport2))
                .build();

        when(countryRepository.findById(USA.getId())).thenReturn(Optional.of(usaWithAirports));

        CountryDto updatedCountry = countryService.switchCountryStatus(USA.getId());

        assertNotNull(updatedCountry);
        assertEquals(USA.getId(), updatedCountry.id());
        assertEquals(USA.getName(), updatedCountry.name());
        assertFalse(updatedCountry.enabled());

        assertFalse(airport1.isEnabled());
        assertFalse(airport2.isEnabled());

        verify(countryRepository, times(1)).findById(USA.getId());
        verify(countryRepository, times(1)).save(usaWithAirports);
    }

    @Test
    void switchCountryStatus_WhenCountryDoesNotExist_ShouldThrowException() {
        when(countryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CountryDoesNotExistException.class, () -> countryService.switchCountryStatus(1));
        verify(countryRepository, never()).save(any());
    }

    @Test
    void disableAllAirportsFromCountry_WhenCountryHasEnabledAirports_ShouldDisableAll() {
        Airport enabledAirport = Airport.builder()
                .id(null)
                .name("JFK Airport")
                .IATACode("JFK")
                .city("New York")
                .enabled(true)
                .country(USA)
                .build();

        Airport alreadyDisabledAirport = Airport.builder()
                .id(null)
                .name("LAX Airport")
                .IATACode("LAX")
                .city("Los Angeles")
                .enabled(false)
                .country(USA)
                .build();

        Country usaWithAirports = Country.builder()
                .id(USA.getId())
                .name(USA.getName())
                .enabled(USA.isEnabled())
                .airports(Arrays.asList(enabledAirport, alreadyDisabledAirport))
                .build();

        when(countryRepository.findById(USA.getId())).thenReturn(Optional.of(usaWithAirports));

        countryService.switchCountryStatus(USA.getId());

        assertFalse(enabledAirport.isEnabled());
        assertFalse(alreadyDisabledAirport.isEnabled());
    }

    @Test
    void getAllCountriesNames_WhenCountriesExist_ShouldReturnAllNames() {
        when(countryRepository.findAll()).thenReturn(Arrays.asList(USA, GERMANY, FRANCE));

        List<String> countryNames = countryService.getAllCountriesNames();

        assertNotNull(countryNames);
        assertEquals(3, countryNames.size());
        assertEquals(USA.getName(), countryNames.get(0));
        assertEquals(GERMANY.getName(), countryNames.get(1));
        assertEquals(FRANCE.getName(), countryNames.get(2));
        verify(countryRepository, times(1)).findAll();
    }

    @Test
    void getAllCountriesNames_WhenNoCountriesExist_ShouldReturnEmptyList() {
        when(countryRepository.findAll()).thenReturn(Collections.emptyList());

        List<String> countryNames = countryService.getAllCountriesNames();

        assertNotNull(countryNames);
        assertEquals(0, countryNames.size());
        verify(countryRepository, times(1)).findAll();
    }

    @Test
    void getAllEnabledCountries_WhenEnabledCountriesExist_ShouldReturnEnabledCountries() {
        when(countryRepository.findAllByEnabledTrue()).thenReturn(Arrays.asList(USA, GERMANY));

        List<Country> enabledCountries = countryService.getAllEnabledCountries();

        assertNotNull(enabledCountries);
        assertEquals(2, enabledCountries.size());
        assertEquals(USA.getId(), enabledCountries.get(0).getId());
        assertEquals(USA.getName(), enabledCountries.get(0).getName());
        assertTrue(enabledCountries.get(0).isEnabled());

        assertEquals(GERMANY.getId(), enabledCountries.get(1).getId());
        assertEquals(GERMANY.getName(), enabledCountries.get(1).getName());
        assertTrue(enabledCountries.get(1).isEnabled());

        verify(countryRepository, times(1)).findAllByEnabledTrue();
    }

    @Test
    void getAllEnabledCountries_WhenNoEnabledCountriesExist_ShouldReturnEmptyList() {
        when(countryRepository.findAllByEnabledTrue()).thenReturn(Collections.emptyList());

        List<Country> enabledCountries = countryService.getAllEnabledCountries();

        assertNotNull(enabledCountries);
        assertEquals(0, enabledCountries.size());
        verify(countryRepository, times(1)).findAllByEnabledTrue();
    }

    @Test
    void isCountryEnabled_WhenCountryIsEnabled_ShouldReturnTrue() {
        when(countryRepository.existsByNameAndEnabledTrue(USA.getName())).thenReturn(true);

        boolean isEnabled = countryService.isCountryEnabled(USA.getName());

        assertTrue(isEnabled);
        verify(countryRepository, times(1)).existsByNameAndEnabledTrue(USA.getName());
    }

    @Test
    void isCountryEnabled_WhenCountryIsNotEnabled_ShouldReturnFalse() {
        when(countryRepository.existsByNameAndEnabledTrue(FRANCE.getName())).thenReturn(false);

        boolean isEnabled = countryService.isCountryEnabled(FRANCE.getName());

        assertFalse(isEnabled);
        verify(countryRepository, times(1)).existsByNameAndEnabledTrue(FRANCE.getName());
    }

}