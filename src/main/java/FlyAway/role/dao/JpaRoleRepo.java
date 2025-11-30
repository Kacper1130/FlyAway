package FlyAway.role.dao;

import FlyAway.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
