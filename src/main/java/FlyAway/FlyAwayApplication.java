package FlyAway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlyAwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlyAwayApplication.class, args);
	}

	//TODO
	// checking if user is not deleted when creating reservation
	// historia rezerwacji, anulowane i wykorzystane
	// uwagi uzytkownikow

//	@Bean
//	public CommandLineRunner commandLineRunner(FlightService service){
//		return args -> {
//			Flight flight = Flight.builder()
//					.departureCity("Warsaw")
//					.arrivalCity("New York")
//					.departureDate(LocalDateTime.now())
//					.arrivalDate(LocalDateTime.of(2024, Month.APRIL, 12, 6, 30))
//					.airline("LOT")
//					.build();
//			service.addFlight(flight);
//
//			Flight flight2 = Flight.builder()
//					.departureCity("Warsaw")
//					.arrivalCity("London")
//					.departureDate(LocalDateTime.now())
//					.arrivalDate(LocalDateTime.of(2024, Month.APRIL, 12, 2, 45))
//					.airline("LOT")
//					.build();
//			service.addFlight(flight2);
//
//		};
//	}
}
