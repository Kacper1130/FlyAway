package FlyAway.flight.country;

import FlyAway.flight.country.dto.CountryDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
@Tag(name = "Country")
public class CountryController {

    private final CountryService countryService;
    private final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<CountryDto> getAllCountries() {
        List<CountryDto> countries = countryService.getAllCountries();
        LOGGER.info("Retrieved {} countries", countries.size());
        return countries;
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CountryDto switchCountryStatus(@PathVariable Integer id) {
        LOGGER.info("Switching status of country id {}", id);
        return countryService.switchCountryStatus(id);
    }

    @GetMapping("/enabled")
    public List<Country> getAllEnabledCountries() {
        List<Country> countries = countryService.getAllEnabledCountries();
        LOGGER.info("Retrieved {} enabled countries", countries.size());
        return countries;
    }

    @GetMapping("/names")
    public List<String> getAllCountriesNames() {
        List<String> countries = countryService.getAllCountriesNames();
        LOGGER.debug("Retrieved {} countries names", countries.size());
        return countries;
    }

    @GetMapping("/is-enabled")
    public boolean isCountryEnabled(@RequestParam String countryName) {
        LOGGER.debug("Checking status of country {}", countryName);
        return countryService.isCountryEnabled(countryName);
    }

}
