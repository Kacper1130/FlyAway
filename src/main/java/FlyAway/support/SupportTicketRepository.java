package FlyAway.support;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SupportTicketRepository extends MongoRepository<SupportTicket, String> {

    List<SupportTicket> findByClientIdOrderByCreatedAtDesc(Long id);

    List<SupportTicket> findByEmployeeIdOrStatus(Long id, TicketStatus status );

    Integer countByEmployeeIdAndStatus(Long employeeId, TicketStatus status);
    Integer countByStatus(TicketStatus status);

}
