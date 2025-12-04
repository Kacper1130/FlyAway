package FlyAway.user.dao;

import FlyAway.role.Role;
import FlyAway.user.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("sql")
public class SqlUserAdapter implements UserRepository {

    private final JpaUserRepository jpaRepo; // Używamy narzędzia z Kroku 1

    public SqlUserAdapter(JpaUserRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    public User save(User user) {
        return jpaRepo.save(user);
    }

    public Optional<User> findById(UUID id) {
        return jpaRepo.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepo.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepo.existsByEmail(email);
    }

    @Override
    public List<User> findAllActiveUsersByRole(Role roleName) {
        return jpaRepo.findAllActiveByRole(roleName);
    }

    @Override
    public List<User> findAllDeletedUsersByRole(Role roleName) {
        return jpaRepo.findAllDeletedByRole(roleName);
    }

    @Override
    public Optional<User> findActiveById(UUID id) {
        return jpaRepo.findActiveById(id);
    }

    @Override
    public List<User> findAllUsersByRole(Role roleName) {
        return jpaRepo.findAllByRole(roleName);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepo.existsById(id);
    }

    @Override
    public long count() {
        return jpaRepo.count();
    }
}
