package FlyAway;

import FlyAway.admin.Admin;
import FlyAway.admin.AdminRepository;
import FlyAway.exception.RoleNotInitializedException;
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
            UserRepository userRepository) {
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
        };
    }

}
