package FlyAway.flight.airport.dao;

import FlyAway.flight.airport.Airport;
import FlyAway.flight.airport.AirportDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
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
public class MongoAirportAdapter implements AirportRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Airport save(Airport airport) {
        AirportDocument doc = toDocument(airport);

        if (doc.getId() == null) {
            doc.setId(UUID.randomUUID());
        }

        mongoTemplate.save(doc);
        return toDomain(doc);
    }

    @Override
    public Optional<Airport> findById(UUID id) {
        AirportDocument doc = mongoTemplate.findById(id, AirportDocument.class);
        return Optional.ofNullable(doc).map(this::toDomain);
    }

    @Override
    public Optional<Airport> findAirportByIATACode(String iataCode) {
        Query query = new Query(Criteria.where("IATACode").is(iataCode));
        AirportDocument doc = mongoTemplate.findOne(query, AirportDocument.class);
        return Optional.ofNullable(doc).map(this::toDomain);
    }

    @Override
    public List<Airport> findAllSortedByCountry() {
        Query query = new Query();

        // SQL: ORDER BY country.name DESC
        // Mongo: Sortowanie po zagnieżdżonym polu
        query.with(Sort.by(Sort.Direction.DESC, "country.name"));

        return mongoTemplate.find(query, AirportDocument.class)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Airport> findAllByEnabledTrue() {
        Query query = new Query(Criteria.where("enabled").is(true));
        return mongoTemplate.find(query, AirportDocument.class)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<Airport> airports) {
        // Konwertujemy listę domenową na listę dokumentów
        List<AirportDocument> docs = airports.stream()
                .map(airport -> {
                    AirportDocument doc = toDocument(airport);
                    if (doc.getId() == null) doc.setId(UUID.randomUUID());
                    return doc;
                })
                .toList();

        // MongoTemplate ma metodę insertAll do szybkiego zapisu listy
        mongoTemplate.insertAll(docs);
    }

    // --- MAPPERY ---

    private AirportDocument toDocument(Airport airport) {
        if (airport == null) return null;

        return AirportDocument.builder()
                .id(airport.getId())
                .name(airport.getName())
                .city(airport.getCity())
                .IATACode(airport.getIATACode()) // Przepisujemy dokładnie
                .enabled(airport.isEnabled())
                .country(airport.getCountry()) // Przekazujemy cały obiekt Country
                .build();
    }

    private Airport toDomain(AirportDocument doc) {
        if (doc == null) return null;

        return Airport.builder()
                .id(doc.getId())
                .name(doc.getName())
                .city(doc.getCity())
                .IATACode(doc.getIATACode())
                .enabled(doc.isEnabled())
                .country(doc.getCountry()) // Odzyskujemy obiekt Country z JSON-a
                .build();
    }
}
