package FlyAway.role.dao;

import FlyAway.role.Role;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("sql")
public class SqlRoleAdapter implements RoleRepository {

    private final JpaRoleRepo jpaRepo;

    public SqlRoleAdapter(JpaRoleRepo jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Role save(Role role) {
        return jpaRepo.save(role);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRepo.findByName(name);
    }
}
