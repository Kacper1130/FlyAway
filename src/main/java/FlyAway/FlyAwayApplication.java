package FlyAway;

import FlyAway.admin.Admin;
import FlyAway.admin.AdminRepository;
import FlyAway.flight.Flight;
import FlyAway.flight.FlightRepository;
import FlyAway.flight.FlightService;
import FlyAway.flight.aircraft.Aircraft;
import FlyAway.flight.aircraft.AircraftRepository;
import FlyAway.flight.aircraft.CabinClass;
import FlyAway.flight.aircraft.SeatClassRange;
import FlyAway.flight.airport.Airport;
import FlyAway.flight.airport.AirportRepository;
import FlyAway.flight.country.CountryRepository;
import FlyAway.role.Role;
import FlyAway.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
@EnableAsync
public class FlyAwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlyAwayApplication.class, args);
	}

	//TODO
	// active reservations
	// historia rezerwacji, anulowane i wykorzystane
	// uwagi uzytkownikow
	// employee wysyla maila do wielu uzytkownikow

//	@Bean
//	CommandLineRunner commandLineRunner(
//			FlightRepository flightRepository,
//			AircraftRepository aircraftRepository,
//			AirportRepository airportRepository,
//			CountryRepository countryRepository
//	) {
//		return args -> {
//			Airport departureAirport = new Airport();
//			departureAirport.setName("Warsaw Chopin Airport");
//			departureAirport.setIATACode("WAW");
//			departureAirport.setCity("Warsaw");
//			departureAirport.setCountry(countryRepository.findById(165).orElseThrow());
//
//			Airport arrivalAirport = new Airport();
//			arrivalAirport.setName("Heathrow Airport");
//			arrivalAirport.setIATACode("LHR");
//			arrivalAirport.setCity("London");
//			arrivalAirport.setCountry(countryRepository.findById(219).orElseThrow());
//
//			airportRepository.saveAll(List.of(departureAirport,arrivalAirport));
//
//
//			Aircraft aircraft1 = new Aircraft();
//			aircraft1.setModel("Boeing 737");
//			aircraft1.setProductionYear(2007);
//			aircraft1.setRegistration("SP-FNA");
//			aircraft1.setTotalSeats(130);
//
//			Map<CabinClass, SeatClassRange> seatClassRanges = new HashMap<>();
//			seatClassRanges.put(CabinClass.BUSINESS, new SeatClassRange(1,20));
//			seatClassRanges.put(CabinClass.ECONOMY, new SeatClassRange(21,130));
//			aircraft1.setSeatClassRanges(seatClassRanges);
//			aircraftRepository.save(aircraft1);
//
//			Aircraft aircraft2 = new Aircraft();
//			aircraft2.setModel("Boeing 777");
//			aircraft2.setProductionYear(2019);
//			aircraft2.setRegistration("SP-FNB");
//			aircraft2.setTotalSeats(340);
//
//			Map<CabinClass, SeatClassRange> seatClassRanges2 = new HashMap<>();
//			seatClassRanges2.put(CabinClass.FIRST, new SeatClassRange(1,24));
//			seatClassRanges2.put(CabinClass.BUSINESS, new SeatClassRange(25,81));
//			seatClassRanges2.put(CabinClass.ECONOMY, new SeatClassRange(82,340));
//			aircraft2.setSeatClassRanges(seatClassRanges2);
//			aircraftRepository.save(aircraft2);
//
//			Flight flight = new Flight();
//
//			Airport airport1 = airportRepository.findAirportByIATACode("WAW").orElseThrow();
//			Airport airport2 = airportRepository.findAirportByIATACode("LHR").orElseThrow();
//
//			flight.setDepartureAirport(departureAirport);
//			flight.setArrivalAirport(arrivalAirport);
//			flight.setDepartureDate(LocalDateTime.of(2025,9,11,12,30));
//			flight.setArrivalDate(LocalDateTime.of(2025,9,11,15,30));
//
//			Aircraft aircraft = aircraftRepository.findAircraftByRegistration("SP-FNB").orElseThrow();
//			flight.setAircraft(aircraft);
//
//			Map<CabinClass, BigDecimal> cabinClassPrices = new HashMap<>();
//			cabinClassPrices.put(CabinClass.BUSINESS, new BigDecimal(100));
//			cabinClassPrices.put(CabinClass.ECONOMY, new BigDecimal(75));
//
//			flight.setCabinClassPrices(cabinClassPrices);
//			flight.setReservations(null);
//			flightRepository.save(flight);
//		};
//	}

//	@Bean
//	public CommandLineRunner commandLineRunner(
//			RoleRepository roleRepository,
//			PasswordEncoder passwordEncoder,
//			AdminRepository adminRepository
//	){
//		return args -> {
//
//			Role client = new Role();
//			client.setName("ROLE_CLIENT");
//
//			Role employee = new Role();
//			employee.setName("ROLE_EMPLOYEE");
//
//			Role admin = new Role();
//			admin.setName("ROLE_ADMIN");
//
//			roleRepository.save(client);
//			roleRepository.save(employee);
//			roleRepository.save(admin);
//
//			var adminRole = roleRepository.findByName("ROLE_ADMIN")
//					.orElseThrow(() -> new IllegalStateException("ROLE ADMIN was not initialized"));
//
//			Admin admin1 = Admin.builder()
//					.firstname("admin")
//					.lastname("admin")
//					.email("admin@flyaway.com")
//					.password(passwordEncoder.encode("tajnehaslo123$"))
//					.phoneNumber("1234567890")
//					.roles(Set.of(adminRole))
//					.enabled(true)
//					.createdAccounts(null)
//					.build();
//
//			adminRepository.save(admin1);
//		};
//	}
}
