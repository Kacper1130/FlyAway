package FlyAway.flight.country.dao;

import FlyAway.flight.country.Country;
import FlyAway.flight.country.CountryDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public class MongoCountryAdapter implements CountryRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Country save(Country country) {
        CountryDocument doc = toDocument(country);

        // Generujemy UUID, jeśli to nowy obiekt
        if (doc.getId() == null) {
            doc.setId(UUID.randomUUID());
        }

        mongoTemplate.save(doc);
        return toDomain(doc);
    }

    @Override
    public Optional<Country> findById(UUID id) {
        CountryDocument doc = mongoTemplate.findById(id, CountryDocument.class);
        return Optional.ofNullable(doc).map(this::toDomain);
    }

    @Override
    public Optional<Country> findByName(String name) {
        // SQL: WHERE name = ?
        Query query = new Query(Criteria.where("name").is(name));
        CountryDocument doc = mongoTemplate.findOne(query, CountryDocument.class);
        return Optional.ofNullable(doc).map(this::toDomain);
    }

    @Override
    public boolean existsByNameAndEnabledTrue(String name) {
        // SQL: WHERE name = ? AND enabled = true
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        query.addCriteria(Criteria.where("enabled").is(true));

        return mongoTemplate.exists(query, CountryDocument.class);
    }

    @Override
    public List<Country> findAllByEnabledTrue() {
        // SQL: WHERE enabled = true
        Query query = new Query(Criteria.where("enabled").is(true));

        return mongoTemplate.find(query, CountryDocument.class)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Country> findAllSorted() {
        // SQL: ORDER BY enabled DESC, name ASC
        Query query = new Query();
        query.with(Sort.by(
                Sort.Order.desc("enabled"),
                Sort.Order.asc("name")
        ));

        return mongoTemplate.find(query, CountryDocument.class)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // --- MAPPERY ---

    private CountryDocument toDocument(Country country) {
        if (country == null) return null;
        return CountryDocument.builder()
                .id(country.getId())
                .name(country.getName())
                .enabled(country.isEnabled())
                .build();
    }

    private Country toDomain(CountryDocument doc) {
        if (doc == null) return null;
        return Country.builder()
                .id(doc.getId())
                .name(doc.getName())
                .enabled(doc.isEnabled())
                .airports(new ArrayList<>()) // Mongo nie trzyma listy lotnisk w kraju, zwracamy pustą listę
                .build();
    }
}