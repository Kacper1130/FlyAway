package FlyAway.flight;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FlightServiceTest {

    @Autowired
    FlightService flightService;

    @Mock
    FlightRepository flightRepository;


}