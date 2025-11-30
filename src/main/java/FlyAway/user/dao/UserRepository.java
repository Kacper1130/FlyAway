package FlyAway.user.dao;

import FlyAway.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository  {

    User save(User user);
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);


    List<User> findAllActiveUsersByRole(String roleName);
    List<User> findAllDeletedUsersByRole(String roleName);
    Optional<User> findActiveById(Long id);

    List<User> findAllUsersByRole(String roleName);

    boolean existsById(Long id);

    long count();
}
