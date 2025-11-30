package FlyAway.user.dao;

import FlyAway.role.Role;
import FlyAway.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository  {

    User save(User user);
    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);


    List<User> findAllActiveUsersByRole(Role roleName);
    List<User> findAllDeletedUsersByRole(Role roleName);

    Optional<User> findActiveById(UUID id);

    List<User> findAllUsersByRole(Role roleName);

    boolean existsById(UUID id);

    long count();
}
