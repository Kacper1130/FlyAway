package FlyAway;

import FlyAway.admin.Admin;
import FlyAway.admin.AdminRepository;
import FlyAway.exception.RoleNotInitializedException;
import FlyAway.flight.Flight;
import FlyAway.flight.FlightRepository;
import FlyAway.flight.aircraft.Aircraft;
import FlyAway.flight.aircraft.AircraftRepository;
import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.aircraft.SeatClassRange;
import FlyAway.flight.airport.Airport;
import FlyAway.flight.airport.AirportRepository;
import FlyAway.flight.country.CountryRepository;
import FlyAway.role.Role;
import FlyAway.role.RoleRepository;
import FlyAway.user.UserRepository;
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
import java.util.Set;

@SpringBootApplication
@EnableAsync
public class FlyAwayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlyAwayApplication.class, args);
    }

    @Bean
    @Profile("!test")
    public CommandLineRunner commandLineRunner(
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AdminRepository adminRepository,
            UserRepository userRepository,
            AircraftRepository aircraftRepository,
            AirportRepository airportRepository,
            CountryRepository countryRepository,
            FlightRepository flightRepository
            ) {
        return args -> {

            if (userRepository.count() != 0) return;

            Role client = new Role();
            client.setName("ROLE_CLIENT");

            Role employee = new Role();
            employee.setName("ROLE_EMPLOYEE");

            Role admin = new Role();
            admin.setName("ROLE_ADMIN");

            roleRepository.save(client);
            roleRepository.save(employee);
            roleRepository.save(admin);

            var adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RoleNotInitializedException("ROLE_ADMIN"));

            Admin admin1 = Admin.builder()
                    .firstname("admin")
                    .lastname("admin")
                    .email("admin@flyaway.com")
                    .password(passwordEncoder.encode("password"))
                    .phoneNumber("1234567890")
                    .roles(Set.of(adminRole))
                    .enabled(true)
                    .createdAccounts(null)
                    .build();

            adminRepository.save(admin1);

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

            aircraftRepository.save(aircraft);

            Airport warsawAirport = Airport.builder()
                    .name("Warsaw Chopin Airport")
                    .IATACode("WAW")
                    .city("Warsaw")
                    .enabled(true)
                    .country(countryRepository.findByName("Poland").get())
                    .build();

            Airport newYorkAirport = Airport.builder()
                    .name("John F. Kennedy International Airport")
                    .IATACode("JFK")
                    .city("New York")
                    .enabled(true)
                    .country(countryRepository.findByName("United States").get())
                    .build();

            Airport londonAirport = Airport.builder()
                    .name("Heathrow Airport")
                    .IATACode("LHR")
                    .city("London")
                    .enabled(true)
                    .country(countryRepository.findByName("United Kingdom").get())
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
                    .aircraft(aircraft)
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
                    .aircraft(aircraft)
                    .cabinClassPrices(cabinClassPrices2)
                    .build();

            flightRepository.saveAll(List.of(flight1, flight2));

        };
    }

}
