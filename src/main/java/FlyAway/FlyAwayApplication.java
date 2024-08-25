package FlyAway;

import FlyAway.flight.Flight;
import FlyAway.flight.FlightService;
import FlyAway.flight.dto.FlightDto;
import FlyAway.role.Role;
import FlyAway.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@SpringBootApplication
public class FlyAwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlyAwayApplication.class, args);
	}

	//TODO
	// active reservations
	// historia rezerwacji, anulowane i wykorzystane
	// uwagi uzytkownikow

//	@Bean
//	public CommandLineRunner commandLineRunner(FlightService service, RoleRepository roleRepository){
//		return args -> {
//			FlightDto flight = new FlightDto(
//					"Warsaw",
//					"New York",
//					LocalDateTime.of(2025, Month.APRIL, 12, 6, 30),
//					LocalDateTime.of(2025, Month.APRIL, 12, 15, 30),
//					"LOT"
//			);
//			service.addFlight(flight);
//
//			FlightDto flight2 = new FlightDto(
//					"Warsaw",
//					"London",
//					LocalDateTime.of(2025, Month.APRIL, 15, 8, 00),
//					LocalDateTime.of(2025, Month.APRIL, 15, 10, 30),
//					"LOT"
//			);
//			service.addFlight(flight2);
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
//		};
//	}
}
