package FlyAway.support;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SupportTicketRepository extends MongoRepository<SupportTicket, String> {

    List<SupportTicket> findByClientIdOrderByCreatedAtDesc(Long id);

}
