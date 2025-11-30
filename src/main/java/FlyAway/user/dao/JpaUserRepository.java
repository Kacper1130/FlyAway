package FlyAway.user.dao;

import FlyAway.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);


    // Tłumaczymy: "Active" to deleted = false, a "ByRole" to JOIN z tabelą ról
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName AND u.deleted = false")
    List<User> findAllActiveByRole(@Param("roleName") String roleName);

    // Tłumaczymy: "Deleted" to deleted = true
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName AND u.deleted = true")
    List<User> findAllDeletedByRole(@Param("roleName") String roleName);

    // Tłumaczymy: "ActiveById" to id = ... i deleted = false
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deleted = false")
    Optional<User> findActiveById(@Param("id") Long id);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findAllByRole(@Param("roleName") String roleName);
}
