package FlyAway;

import FlyAway.flight.Flight;
import FlyAway.flight.FlightService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.Month;

@SpringBootApplication
public class FlyAwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlyAwayApplication.class, args);
	}

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
