package FlyAway;

import FlyAway.flight.Flight;
import FlyAway.flight.dao.FlightRepository; // Pamiętaj o nowych importach (dao/repository)
import FlyAway.flight.aircraft.Aircraft;
import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.aircraft.SeatClassRange;
import FlyAway.flight.aircraft.dao.AircraftRepository;
import FlyAway.flight.airport.Airport;
import FlyAway.flight.airport.dao.AirportRepository;
import FlyAway.flight.country.Country;
import FlyAway.flight.country.dao.CountryRepository;
import FlyAway.role.Role;
import FlyAway.user.User;
import FlyAway.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Profile("!test") // Nie uruchamiaj w testach jednostkowych
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AircraftRepository aircraftRepository;
    private final AirportRepository airportRepository;
    private final CountryRepository countryRepository;
    private final FlightRepository flightRepository;

    @Override
    public void run(String... args) throws Exception {

        // 1. Bezpiecznik
        if (userRepository.count() != 0) return;

        System.out.println("--- ROZPOCZYNAM SEEDOWANIE DANYCH ---");

        // 2. Kraje
        Country poland = countryRepository.save(Country.builder().name("Poland").enabled(true).build());
        Country usa = countryRepository.save(Country.builder().name("United States").enabled(true).build());
        Country uk = countryRepository.save(Country.builder().name("United Kingdom").enabled(true).build());

        // 3. User (Admin)
        User admin1 = User.builder()
                .firstname("admin")
                .lastname("admin")
                .email("admin@flyaway.com")
                .password(passwordEncoder.encode("password"))
                .phoneNumber("1234567890")
                .role(Role.ROLE_ADMIN)
                .enabled(true)
                .build();
        userRepository.save(admin1);

        // 4. Samolot
        Map<CabinClass, SeatClassRange> seatClassRanges = new HashMap<>();
        seatClassRanges.put(CabinClass.FIRST, new SeatClassRange(1, 10));
        seatClassRanges.put(CabinClass.BUSINESS, new SeatClassRange(11, 40));
        seatClassRanges.put(CabinClass.ECONOMY, new SeatClassRange(31, 320));

        Aircraft aircraft = Aircraft.builder()
                .model("Boeing 777")
                .productionYear(2010)
                .registration("SP-AA1")
                .totalSeats(320)
                .seatClassRanges(seatClassRanges)
                .build();

        // WAŻNE: Przypisujemy wynik save do zmiennej (żeby mieć ID w Mongo)
        Aircraft savedAircraft = aircraftRepository.save(aircraft);

        // 5. Lotniska
        // WAŻNE: Zapisujemy pojedynczo i przypisujemy do zmiennych, żeby mieć pewność, że mamy ID!
        Airport warsawAirport = airportRepository.save(Airport.builder()
                .name("Warsaw Chopin Airport")
                .IATACode("WAW")
                .city("Warsaw")
                .enabled(true)
                .country(poland)
                .build());

        Airport newYorkAirport = airportRepository.save(Airport.builder()
                .name("JFK Airport")
                .IATACode("JFK")
                .city("New York")
                .enabled(true)
                .country(usa)
                .build());

        Airport londonAirport = airportRepository.save(Airport.builder()
                .name("Heathrow Airport")
                .IATACode("LHR")
                .city("London")
                .enabled(true)
                .country(uk)
                .build());

        // 6. Loty (Przykładowe)
        createFlight(warsawAirport, newYorkAirport, savedAircraft, LocalDateTime.of(2026, 2, 11, 10, 20));
        createFlight(warsawAirport, londonAirport, savedAircraft, LocalDateTime.of(2026, 4, 26, 8, 0));

        // 7. GENEROWANIE MASOWE (To odkomentujesz za chwilę!)
//         generateMassiveData(List.of(warsawAirport, newYorkAirport, londonAirport), savedAircraft);

        System.out.println("--- SEEDOWANIE ZAKOŃCZONE ---");
    }

    // Pomocnicza metoda do tworzenia pojedynczego lotu
    private void createFlight(Airport from, Airport to, Aircraft aircraft, LocalDateTime departure) {
        Map<CabinClass, BigDecimal> prices = new HashMap<>();
        prices.put(CabinClass.FIRST, BigDecimal.valueOf(1300));
        prices.put(CabinClass.BUSINESS, BigDecimal.valueOf(600));
        prices.put(CabinClass.ECONOMY, BigDecimal.valueOf(300));

        Flight flight = Flight.builder()
                .departureAirport(from)
                .arrivalAirport(to)
                .departureDate(departure)
                .arrivalDate(departure.plusHours(8))
                .aircraft(aircraft)
                .cabinClassPrices(prices)
                .build();

        flightRepository.save(flight);
    }
}

