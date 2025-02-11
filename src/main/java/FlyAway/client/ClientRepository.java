package FlyAway.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);
    @Query("SELECT u FROM Client u WHERE u.deleted = false")
    List<Client> findAllActiveClients();

    @Query("SELECT u FROM Client u WHERE u.deleted = true")
    List<Client> findAllDeletedClients();

    @Query("SELECT u FROM Client u WHERE u.id = :id AND u.deleted = false")
    Optional<Client> findActiveById(@Param("id") Long id);

    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.reservations WHERE c.id = :clientId")
    Optional<Client> findByIdWithReservations(@Param("clientId") Long clientId);
}
