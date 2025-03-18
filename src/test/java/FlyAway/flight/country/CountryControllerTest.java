package FlyAway.flight.country;

import FlyAway.WithMockAdmin;
import FlyAway.WithMockClient;
import FlyAway.exception.CountryDoesNotExistException;
import FlyAway.flight.country.dto.CountryDto;
import FlyAway.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
class CountryControllerTest {
    private static final CountryDto USA = new CountryDto(1, "United States", true);
    private static final CountryDto GERMANY = new CountryDto(2, "Germany", true);
    private static final CountryDto FRANCE = new CountryDto(3, "France", false);

    private static final Country USA_ENTITY = new Country(1, "United States", true, null);
    private static final Country GERMANY_ENTITY = new Country(2, "Germany", true, null);

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CountryService countryService;

    @MockBean
    private JwtService jwtService;

    @Test
    @WithMockClient
    void getAllCountries_WhenCountriesExist_ShouldReturnCountryList() throws Exception {
        List<CountryDto> countries = List.of(USA, GERMANY, FRANCE);

        when(countryService.getAllCountries()).thenReturn(countries);

        mockMvc.perform(get("/api/v1/countries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))

                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("United States"))
                .andExpect(jsonPath("$[0].enabled").value(true))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Germany"))
                .andExpect(jsonPath("$[1].enabled").value(true))

                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].name").value("France"))
                .andExpect(jsonPath("$[2].enabled").value(false));

        verify(countryService, times(1)).getAllCountries();
    }

    @Test
    @WithMockClient
    void getAllCountries_WhenNoCountriesExist_ShouldReturnEmptyList() throws Exception {
        when(countryService.getAllCountries()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/countries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(countryService, times(1)).getAllCountries();
    }

    @Test
    @WithMockAdmin
    void switchCountryStatus_WhenCountryExists_ShouldToggleStatus() throws Exception {
        CountryDto updatedCountry = new CountryDto(USA.id(), USA.name(), !USA.enabled());

        when(countryService.switchCountryStatus(USA.id())).thenReturn(updatedCountry);

        mockMvc.perform(patch("/api/v1/countries/{id}", USA.id())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USA.id()))
                .andExpect(jsonPath("$.name").value(USA.name()))
                .andExpect(jsonPath("$.enabled").value(!USA.enabled()));

        verify(countryService, times(1)).switchCountryStatus(USA.id());
    }

    @Test
    @WithMockAdmin
    void switchCountryStatus_WhenCountryDoesNotExist_ShouldThrowException() throws Exception {
        Integer nonExistentId = 999;
        when(countryService.switchCountryStatus(nonExistentId)).thenThrow(new CountryDoesNotExistException());

        mockMvc.perform(patch("/api/v1/countries/{id}", nonExistentId)
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Country with given id does not exist"));

        verify(countryService, times(1)).switchCountryStatus(nonExistentId);
    }

    @Test
    @WithMockClient
    void getAllEnabledCountries_WhenEnabledCountriesExist_ShouldReturnOnlyEnabledCountries() throws Exception {
        List<Country> enabledCountries = Arrays.asList(USA_ENTITY, GERMANY_ENTITY);

        when(countryService.getAllEnabledCountries()).thenReturn(enabledCountries);

        mockMvc.perform(get("/api/v1/countries/enabled"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("United States"))
                .andExpect(jsonPath("$[0].enabled").value(true))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Germany"))
                .andExpect(jsonPath("$[1].enabled").value(true));

        verify(countryService, times(1)).getAllEnabledCountries();
    }

    @Test
    @WithMockClient
    void getAllEnabledCountries_WhenNoEnabledCountriesExist_ShouldReturnEmptyList() throws Exception {
        when(countryService.getAllEnabledCountries()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/countries/enabled"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(countryService, times(1)).getAllEnabledCountries();
    }

    @Test
    @WithMockClient
    void getAllCountriesNames_WhenCountriesExist_ShouldReturnAllNames() throws Exception {
        List<String> countryNames = Arrays.asList("United States", "Germany", "France");

        when(countryService.getAllCountriesNames()).thenReturn(countryNames);

        mockMvc.perform(get("/api/v1/countries/names"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0]").value("United States"))
                .andExpect(jsonPath("$[1]").value("Germany"))
                .andExpect(jsonPath("$[2]").value("France"));

        verify(countryService, times(1)).getAllCountriesNames();
    }

    @Test
    @WithMockClient
    void getAllCountriesNames_WhenNoCountriesExist_ShouldReturnEmptyList() throws Exception {
        when(countryService.getAllCountriesNames()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/countries/names"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(countryService, times(1)).getAllCountriesNames();
    }

    @Test
    @WithMockClient
    void isCountryEnabled_WhenCountryIsEnabled_ShouldReturnTrue() throws Exception {
        when(countryService.isCountryEnabled("United States")).thenReturn(true);

        mockMvc.perform(get("/api/v1/countries/is-enabled")
                        .param("countryName", "United States"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(countryService, times(1)).isCountryEnabled("United States");
    }

    @Test
    @WithMockClient
    void isCountryEnabled_WhenCountryIsNotEnabled_ShouldReturnFalse() throws Exception {
        when(countryService.isCountryEnabled("France")).thenReturn(false);

        mockMvc.perform(get("/api/v1/countries/is-enabled")
                        .param("countryName", "France"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(countryService, times(1)).isCountryEnabled("France");
    }
}