package FlyAway.flight.country;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public List<Country> getAllCountries() {
        List<Country> countries = countryService.getAllCountries();
        LOGGER.info("Retrieved {} countries", countries.size());
        return countries;
    }

    @PatchMapping("/{id}")
    public Country switchCountryStatus(@PathVariable Integer id) {
        LOGGER.info("Switching status of country id {}", id);
        return countryService.switchCountryStatus(id);
    }

}
