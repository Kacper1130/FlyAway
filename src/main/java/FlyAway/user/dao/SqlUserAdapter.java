package FlyAway.user.dao;

import FlyAway.user.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("sql") // <--- KLUCZOWE: To jest wtyczka SQL
public class SqlUserAdapter implements UserRepository {

    private final JpaUserRepository jpaRepo; // Używamy narzędzia z Kroku 1

    public SqlUserAdapter(JpaUserRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    public User save(User user) {
        return jpaRepo.save(user);
    }

    public Optional<User> findById(Long id) {
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
    public List<User> findAllActiveUsersByRole(String roleName) {
        return jpaRepo.findAllActiveByRole(roleName);
    }

    @Override
    public List<User> findAllDeletedUsersByRole(String roleName) {
        return jpaRepo.findAllDeletedByRole(roleName);
    }

    @Override
    public Optional<User> findActiveById(Long id) {
        return jpaRepo.findActiveById(id);
    }

    @Override
    public List<User> findAllUsersByRole(String roleName) {
        return jpaRepo.findAllByRole(roleName);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepo.existsById(id);
    }

    @Override
    public long count() {
        return jpaRepo.count();
    }

    // Musisz zaimplementować też save i findById (z JpaRepository)
    // Jeśli nie ma ich w UserRepository, dodaj je tam!
    // @Override
    // public User save(User user) { return jpaRepo.save(user); }
}
