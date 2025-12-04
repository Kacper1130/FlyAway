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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableAsync
public class FlyAwayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlyAwayApplication.class, args);
    }

    @Bean
    @Profile("!test")
    public CommandLineRunner commandLineRunner(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            AircraftRepository aircraftRepository,
            AirportRepository airportRepository,
            CountryRepository countryRepository,
            FlightRepository flightRepository
            ) {
        return args -> {

            if (userRepository.count() != 0) return;

            Country poland = countryRepository.save(
                    Country.builder().name("Poland").enabled(true).build()
            );

            Country usa = countryRepository.save(
                    Country.builder().name("United States").enabled(true).build()
            );

            Country uk = countryRepository.save(
                    Country.builder().name("United Kingdom").enabled(true).build()
            );

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

            var savedAircraft1 = aircraftRepository.save(aircraft);

            Airport warsawAirport = Airport.builder()
                    .name("Warsaw Chopin Airport")
                    .IATACode("WAW")
                    .city("Warsaw")
                    .enabled(true)
                    .country(poland)
                    .build();

            Airport newYorkAirport = Airport.builder()
                    .name("John F. Kennedy International Airport")
                    .IATACode("JFK")
                    .city("New York")
                    .enabled(true)
                    .country(usa)
                    .build();

            Airport londonAirport = Airport.builder()
                    .name("Heathrow Airport")
                    .IATACode("LHR")
                    .city("London")
                    .enabled(true)
                    .country(uk)
                    .build();

            airportRepository.saveAll(List.of(warsawAirport, newYorkAirport, londonAirport));

            Map<CabinClass, BigDecimal> cabinClassPrices1 = new HashMap<>();
            cabinClassPrices1.put(CabinClass.FIRST, BigDecimal.valueOf(1300));
            cabinClassPrices1.put(CabinClass.BUSINESS, BigDecimal.valueOf(600));
            cabinClassPrices1.put(CabinClass.ECONOMY, BigDecimal.valueOf(300));
            Flight flight1 = Flight.builder()
                    .departureAirport(warsawAirport)
                    .arrivalAirport(newYorkAirport)
                    .departureDate(LocalDateTime.of(2026,2,11, 10,20))
                    .arrivalDate(LocalDateTime.of(2026,2,11, 19,50))
                    .aircraft(savedAircraft1)
                    .cabinClassPrices(cabinClassPrices1)
                    .build();

            Map<CabinClass, BigDecimal> cabinClassPrices2 = new HashMap<>();
            cabinClassPrices2.put(CabinClass.FIRST, BigDecimal.valueOf(600));
            cabinClassPrices2.put(CabinClass.BUSINESS, BigDecimal.valueOf(250));
            cabinClassPrices2.put(CabinClass.ECONOMY, BigDecimal.valueOf(80));
            Flight flight2 = Flight.builder()
                    .departureAirport(warsawAirport)
                    .arrivalAirport(londonAirport)
                    .departureDate(LocalDateTime.of(2026,4,26, 8,0))
                    .arrivalDate(LocalDateTime.of(2026,4,26, 10,40))
                    .aircraft(savedAircraft1)
                    .cabinClassPrices(cabinClassPrices2)
                    .build();

            flightRepository.saveAll(List.of(flight1, flight2));

        };
    }

}
