package FlyAway.support;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee/tickets")
@Tag(name = "EmployeeSupportTicket")
@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public class EmployeeSupportTicketController {



}
