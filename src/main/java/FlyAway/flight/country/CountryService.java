package FlyAway.flight.country;

import FlyAway.exception.CountryDoesNotExistException;
import FlyAway.flight.country.dto.CountryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(CountryService.class);

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<CountryDto> getAllCountries() {
        return countryRepository.findAllWithoutAirports();
    }

    protected CountryDto switchCountryStatus(Integer id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(CountryDoesNotExistException::new);
        LOGGER.info("Current status of {} - {}", country.getName(), country.isEnabled());
        country.setEnabled(!country.isEnabled());
        LOGGER.info("Changed status of {} to {}", country.getName(), country.isEnabled());
        countryRepository.save(country);
        return new CountryDto(country.getId(), country.getName(), country.isEnabled());
    }

    public List<Country> getAllEnabledCountries() {
        return countryRepository.findAllByEnabledTrue();
    }
}
