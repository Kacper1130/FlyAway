package FlyAway.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.deleted = false")
    List<User> findAllActiveUsers();

    @Query("SELECT u FROM User u WHERE u.deleted = true")
    List<User> findAllDeletedUsers();

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deleted = false")
    Optional<User> findActiveById(@Param("id") Long id);

}
