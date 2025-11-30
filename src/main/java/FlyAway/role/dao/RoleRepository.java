package FlyAway.role.dao;

import FlyAway.role.Role;

import java.util.Optional;

public interface RoleRepository {
    Role save(Role role);
    Optional<Role> findByName(String name);
}
