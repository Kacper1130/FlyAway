package FlyAway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

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
////			Airport departureAirport = new Airport();
////			departureAirport.setName("Warsaw Chopin Airport");
////			departureAirport.setIATACode("ee1");
////			departureAirport.setCity("Warsaw");
////			departureAirport.setCountry(countryRepository.findById(165).orElseThrow());
////
////			Airport arrivalAirport = new Airport();
////			arrivalAirport.setName("Heathrow Airport");
////			arrivalAirport.setIATACode("ee2");
////			arrivalAirport.setCity("London");
////			arrivalAirport.setCountry(countryRepository.findById(219).orElseThrow());
////
////			airportRepository.saveAll(List.of(departureAirport,arrivalAirport));
//
//
////			Aircraft aircraft1 = new Aircraft();
////			aircraft1.setModel("Boeing 737");
////			aircraft1.setProductionYear(2007);
////			aircraft1.setRegistration("sp-abc");
////			aircraft1.setTotalSeats(130);
////
////			Map<CabinClass, SeatClassRange> seatClassRanges = new HashMap<>();
////			seatClassRanges.put(CabinClass.BUSINESS, new SeatClassRange(1,20));
////			seatClassRanges.put(CabinClass.ECONOMY, new SeatClassRange(21,130));
////			aircraft1.setSeatClassRanges(seatClassRanges);
////			aircraftRepository.save(aircraft1);
//
//			Aircraft aircraft2 = new Aircraft();
//			aircraft2.setModel("Boeing 777");
//			aircraft2.setProductionYear(2019);
//			aircraft2.setRegistration("SP-FNY");
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
//			Airport airport1 = airportRepository.findAirportByIATACode("ee1").orElseThrow();
//			Airport airport2 = airportRepository.findAirportByIATACode("ee2").orElseThrow();
//
//			flight.setDepartureAirport(airport1);
//			flight.setArrivalAirport(airport2);
//			flight.setDepartureDate(LocalDateTime.of(2025,9,11,12,30));
//			flight.setArrivalDate(LocalDateTime.of(2025,9,11,15,30));
//
//			Aircraft aircraft = aircraftRepository.findAircraftByRegistration("SP-FNY").orElseThrow();
//			flight.setAircraft(aircraft);
//
//			Map<CabinClass, BigDecimal> cabinClassPrices = new HashMap<>();
//			cabinClassPrices.put(CabinClass.FIRST, new BigDecimal(200));
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

//	@Bean
//	CommandLineRunner commandLineRunner(AirportRepository airportRepository, CountryRepository countryRepository) {
//		return args -> {
//			Airport airport1 = new Airport(
//					UUID.randomUUID(),
//					"lotnisko nazwa aha",
//					"AHA1",
//					"Warszawa",
//					true,
//					countryRepository.findById(1).get()
//			);
//			Airport airport2 = new Airport(
//					UUID.randomUUID(),
//					"lotnisko nazwa aha",
//					"AHA1",
//					"Krakwo",
//					true,
//					countryRepository.findById(1).get()
//			);
//			Airport airport3 = new Airport(
//					UUID.randomUUID(),
//					"lotniskoo nazwa aha",
//					"AHA12",
//					"Warsawa",
//					false,
//					countryRepository.findById(1).get()
//			);
//			Airport airport4 = new Airport(
//					UUID.randomUUID(),
//					"lotnisko nazwa aha",
//					"AHA1",
//					"Warszawa",
//					true,
//					countryRepository.findById(1).get()
//			);
//			Airport airport5 = new Airport(
//					UUID.randomUUID(),
//					"lodasftnisko nazwa aha",
//					"AHA125",
//					"Berlin",
//					true,
//					countryRepository.findById(2).get()
//			);
//			airportRepository.saveAll(List.of(airport1,airport2,airport3,airport4,airport5));
//		};
//	}

}
