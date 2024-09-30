package FlyAway.flight.country;

import FlyAway.exception.CountryDoesNotExistException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    protected Country switchCountryStatus(Integer id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(CountryDoesNotExistException::new);
        country.setEnabled(!country.isEnabled());
        countryRepository.save(country);
        return country;
    }

}
