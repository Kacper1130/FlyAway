package FlyAway;

import FlyAway.flight.Flight;
import FlyAway.flight.aircraft.Aircraft;
import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.aircraft.SeatClassRange;
import FlyAway.flight.aircraft.dao.AircraftRepository;
import FlyAway.flight.airport.Airport;
import FlyAway.flight.airport.dao.AirportRepository;
import FlyAway.flight.country.Country;
import FlyAway.flight.country.dao.CountryRepository;
import FlyAway.flight.dao.FlightRepository;
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
@Profile("!test")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AircraftRepository aircraftRepository;
    private final AirportRepository airportRepository;
    private final CountryRepository countryRepository;
    private final FlightRepository flightRepository;

    // --- KONFIGURACJA ILOŚCI DANYCH ---
    private static final int FLIGHTS_TO_GENERATE = 5000; // Ilość lotów do wygenerowania
    private static final int BATCH_SIZE = 500;           // Zapisuj co 500 sztuk (dla wydajności)

    @Override
    public void run(String... args) throws Exception {

        // 1. Bezpiecznik - jeśli są dane, nie rób nic
        if (userRepository.count() != 0) {
            System.out.println("--- BAZA DANYCH NIE JEST PUSTA. POMIJAM SEEDOWANIE. ---");
            return;
        }

        System.out.println("--- ROZPOCZYNAM SEEDOWANIE DANYCH PODSTAWOWYCH ---");

        // 2. KRAJE
        Country poland = countryRepository.save(Country.builder().name("Poland").enabled(true).build());
        Country usa = countryRepository.save(Country.builder().name("United States").enabled(true).build());
        Country uk = countryRepository.save(Country.builder().name("United Kingdom").enabled(true).build());
        Country germany = countryRepository.save(Country.builder().name("Germany").enabled(true).build());
        Country france = countryRepository.save(Country.builder().name("France").enabled(true).build());

        // 3. USER (Admin)
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

        // Dodajmy zwykłego Usera pod testy JMetera
        User client1 = User.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .email("user@flyaway.com") // Tego loginu użyjesz w JMeterze
                .password(passwordEncoder.encode("password"))
                .phoneNumber("111222333")
                .role(Role.ROLE_CLIENT)
                .enabled(true)
                .build();
        userRepository.save(client1);

        // 4. SAMOLOTY
        Aircraft b777 = createAircraft("Boeing 777", "SP-AAA", 320);
        Aircraft b737 = createAircraft("Boeing 737", "SP-BBB", 180);
        Aircraft a320 = createAircraft("Airbus A320", "SP-CCC", 150);

        // Tworzymy listę floty do losowania
        List<Aircraft> fleet = List.of(b777, b737, a320);

        // 5. LOTNISKA
        List<Airport> airports = new ArrayList<>();
        airports.add(createAirport("Warsaw Chopin", "WAW", "Warsaw", poland));
        airports.add(createAirport("JFK Airport", "JFK", "New York", usa));
        airports.add(createAirport("Heathrow", "LHR", "London", uk));
        airports.add(createAirport("Berlin Brandenburg", "BER", "Berlin", germany));
        airports.add(createAirport("Charles de Gaulle", "CDG", "Paris", france));

        // 6. GENEROWANIE MASOWE (LOTY)
        generateMassiveData(airports, fleet);

        System.out.println("--- SEEDOWANIE ZAKOŃCZONE SUKCESEM ---");
    }

    // --- METODA GENERUJĄCA TYSIĄCE LOTÓW ---
    private void generateMassiveData(List<Airport> airports, List<Aircraft> fleet) {
        System.out.println("--- GENEROWANIE " + FLIGHTS_TO_GENERATE + " LOTÓW...  ---");
        long start = System.currentTimeMillis();

        List<Flight> batch = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < FLIGHTS_TO_GENERATE; i++) {

            // 1. Losuj lotniska (Wylot != Przylot)
            Airport dep = airports.get(random.nextInt(airports.size()));
            Airport arr = airports.get(random.nextInt(airports.size()));
            while (dep.getId().equals(arr.getId())) {
                arr = airports.get(random.nextInt(airports.size()));
            }

            // 2. Losuj samolot
            Aircraft plane = fleet.get(random.nextInt(fleet.size()));

            // 3. Losuj datę (od TERAZ do +90 dni w przyszłość)
            LocalDateTime departureDate = LocalDateTime.now().plusHours(random.nextInt(24 * 90));
            // Czas lotu: od 1h do 12h
            LocalDateTime arrivalDate = departureDate.plusHours(1 + random.nextInt(11));

            // 4. Losuj ceny (żeby każdy lot był trochę inny)
            Map<CabinClass, BigDecimal> prices = new HashMap<>();
            prices.put(CabinClass.ECONOMY, BigDecimal.valueOf(50 + random.nextInt(200))); // 50-250
            prices.put(CabinClass.BUSINESS, BigDecimal.valueOf(300 + random.nextInt(500))); // 300-800
            prices.put(CabinClass.FIRST, BigDecimal.valueOf(1000 + random.nextInt(1000))); // 1000-2000

            Flight flight = Flight.builder()
                    .departureAirport(dep)
                    .arrivalAirport(arr)
                    .departureDate(departureDate)
                    .arrivalDate(arrivalDate)
                    .aircraft(plane)
                    .cabinClassPrices(prices)
                    .build();

            batch.add(flight);

            // Zapisz paczkę (Batch Save), żeby nie zapchać pamięci
            if (batch.size() >= BATCH_SIZE) {
                flightRepository.saveAll(batch);
                batch.clear();
                System.out.println("... Zapisano " + (i + 1) + " lotów");
            }
        }

        // Zapisz resztę, jeśli coś zostało
        if (!batch.isEmpty()) {
            flightRepository.saveAll(batch);
        }

        long end = System.currentTimeMillis();
        System.out.println("--- WYGENEROWANO LOTY W CZASIE: " + (end - start) + "ms ---");
    }

    // --- POMOCNICZE METODY TWÓRCZE ---

    private Aircraft createAircraft(String model, String reg, int seats) {
        Map<CabinClass, SeatClassRange> config = new HashMap<>();
        config.put(CabinClass.FIRST, new SeatClassRange(1, 10));
        config.put(CabinClass.BUSINESS, new SeatClassRange(11, 40));
        config.put(CabinClass.ECONOMY, new SeatClassRange(41, seats));

        Aircraft a = Aircraft.builder()
                .model(model)
                .productionYear(2015)
                .registration(reg)
                .totalSeats(seats)
                .seatClassRanges(config)
                .build();
        return aircraftRepository.save(a); // Zwracamy zapisany obiekt z ID
    }

    private Airport createAirport(String name, String code, String city, Country country) {
        Airport a = Airport.builder()
                .name(name)
                .IATACode(code)
                .city(city)
                .enabled(true)
                .country(country)
                .build();
        return airportRepository.save(a); // Zwracamy zapisany obiekt z ID
    }
}
