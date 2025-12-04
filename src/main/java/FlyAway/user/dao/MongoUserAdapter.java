package FlyAway.user.dao;

import FlyAway.role.Role;
import FlyAway.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public class MongoUserAdapter implements UserRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public User save(User user) {
        UserDocument doc = toDocument(user);

        if (doc.getId() == null) {
            doc.setId(UUID.randomUUID());
        }

        mongoTemplate.save(doc);
        return toDomain(doc);
    }

    @Override
    public Optional<User> findById(UUID id) {
        UserDocument doc = mongoTemplate.findById(id, UserDocument.class);
        return Optional.ofNullable(doc).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        UserDocument doc = mongoTemplate.findOne(query, UserDocument.class);
        return Optional.ofNullable(doc).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        return mongoTemplate.exists(query, UserDocument.class);
    }

    @Override
    public boolean existsById(UUID id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.exists(query, UserDocument.class);
    }

    @Override
    public long count() {
        return mongoTemplate.count(new Query(), UserDocument.class);
    }

    // --- ZAPYTANIA Z FILTROWANIEM ---

    @Override
    public List<User> findAllActiveUsersByRole(Role role) {
        Query query = new Query();

        // Warunek 1: Rola się zgadza
        query.addCriteria(Criteria.where("role").is(role));

        // Warunek 2: User NIE jest usunięty (deleted == false LUB deleted nie istnieje)
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("deleted").is(false),
                Criteria.where("deleted").exists(false)
        ));

        return mongoTemplate.find(query, UserDocument.class)
                .stream().map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllDeletedUsersByRole(Role role) {
        Query query = new Query();
        query.addCriteria(Criteria.where("role").is(role));
        query.addCriteria(Criteria.where("deleted").is(true)); // Tu szukamy wprost true

        return mongoTemplate.find(query, UserDocument.class)
                .stream().map(this::toDomain)
                .toList();
    }

    @Override
    public List<User> findAllUsersByRole(Role role) {
        Query query = new Query(Criteria.where("role").is(role));
        return mongoTemplate.find(query, UserDocument.class)
                .stream().map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findActiveById(UUID id) {
        Query query = new Query(Criteria.where("id").is(id));

        // Dodajemy warunek aktywności
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("deleted").is(false),
                Criteria.where("deleted").exists(false)
        ));

        UserDocument doc = mongoTemplate.findOne(query, UserDocument.class);
        return Optional.ofNullable(doc).map(this::toDomain);
    }

    // --- MAPPERY (Prywatne metody pomocnicze) ---
    // Przepisują User -> UserDocument i odwrotnie

    private UserDocument toDocument(User user) {
        if (user == null) return null;
        return UserDocument.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .enabled(user.isEnabled())
                .role(user.getRole()) // Enum -> String (automatycznie)
                .dayOfBirth(user.getDayOfBirth())
                .deleted(user.isDeleted())
                .hireDate(user.getHireDate())
                .lastLogin(user.getLastLogin())
                .build();
    }

    private User toDomain(UserDocument doc) {
        if (doc == null) return null;
        return User.builder()
                .id(doc.getId())
                .firstname(doc.getFirstname())
                .lastname(doc.getLastname())
                .email(doc.getEmail())
                .password(doc.getPassword())
                .phoneNumber(doc.getPhoneNumber())
                .enabled(doc.isEnabled())
                .role(doc.getRole())
                .dayOfBirth(doc.getDayOfBirth())
                // Zabezpieczenie przed nullem przy odczycie
                .deleted(doc.getDeleted() != null ? doc.getDeleted() : false)
                .hireDate(doc.getHireDate())
                .lastLogin(doc.getLastLogin())
                .build();
    }
}