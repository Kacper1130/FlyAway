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
}
