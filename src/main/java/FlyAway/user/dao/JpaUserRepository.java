package FlyAway.user.dao;

import FlyAway.role.Role;
import FlyAway.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);


    @Query("SELECT u FROM User u WHERE u.role = :role AND (u.deleted = false OR u.deleted IS NULL)")
    List<User> findAllActiveByRole(@Param("role") Role role);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.deleted = true")
    List<User> findAllDeletedByRole(@Param("role") Role role);

    // 4. Zmiana: Parametr ID to UUID
    @Query("SELECT u FROM User u WHERE u.id = :id AND (u.deleted = false OR u.deleted IS NULL)")
    Optional<User> findActiveById(@Param("id") UUID id);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findAllByRole(@Param("role") Role role);

    boolean existsById(UUID id);

    Optional<User> findById(UUID id);
}
