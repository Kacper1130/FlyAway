package FlyAway.flight.country;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
@Tag(name = "Country")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    @PatchMapping("/{id}")
    public Country switchCountryStatus(@PathVariable Integer id) {
        return countryService.switchCountryStatus(id);
    }

}
