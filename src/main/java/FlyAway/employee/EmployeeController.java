package FlyAway.employee;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee")
@Tag(name = "Employee")
public class EmployeeController {

    @PostMapping("/activate")
    public ResponseEntity<?> activateEmployee() {
        return null;
    }

}

