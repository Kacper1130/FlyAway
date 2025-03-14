package FlyAway.flight.country;

import FlyAway.exception.CountryDoesNotExistException;
import FlyAway.flight.country.dto.CountryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(CountryService.class);

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<CountryDto> getAllCountries() {
        List<CountryDto> countries = countryRepository.findAllWithoutAirports();
        LOGGER.info("Retrieved {} countries", countries.size());
        return countries;
    }

    public CountryDto switchCountryStatus(Integer id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(CountryDoesNotExistException::new);
        LOGGER.info("Current status of {} - {}", country.getName(), country.isEnabled());
        disableAllAirportsFromCountry(country);
        country.setEnabled(!country.isEnabled());
        LOGGER.info("Changed status of {} to {}", country.getName(), country.isEnabled());
        countryRepository.save(country);
        return new CountryDto(country.getId(), country.getName(), country.isEnabled());
    }

    private void disableAllAirportsFromCountry(Country country) {
        country.getAirports().forEach(airport -> {
            if (airport.isEnabled()) {
                airport.setEnabled(false);
                LOGGER.info("Disabled {}", airport.getName());
            }
        });
    }

    public List<String> getAllCountriesNames() {
        List<Country> countries = countryRepository.findAll();
        LOGGER.debug("Full countries size: {}", countries.size());
        List<String> countriesNames = new ArrayList<>();
        countries.forEach(c -> countriesNames.add(c.getName()));
        LOGGER.debug("Countries names size: {}", countriesNames.size());
        return countriesNames;
    }

    public List<Country> getAllEnabledCountries() {
        List<Country> enabledCountries = countryRepository.findAllByEnabledTrue();
        LOGGER.info("Retrieved {} enabled Countries", enabledCountries.size());
        return enabledCountries;
    }

    public boolean isCountryEnabled(String countryName) {
        boolean isEnabled = countryRepository.existsByNameAndEnabledTrue(countryName);
        LOGGER.debug("Country {} is {}", countryName, isEnabled);
        return isEnabled;
    }

}
