package FlyAway.flight.aircraft.dao;

import FlyAway.flight.aircraft.Aircraft;
import FlyAway.flight.aircraft.AircraftDocument;
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
public class MongoAircraftAdapter implements AircraftRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Aircraft save(Aircraft aircraft) {
        AircraftDocument doc = toDocument(aircraft);

        if (doc.getId() == null) {
            doc.setId(UUID.randomUUID());
        }

        mongoTemplate.save(doc);
        return toDomain(doc);
    }

    @Override
    public Optional<Aircraft> findById(UUID id) {
        AircraftDocument doc = mongoTemplate.findById(id, AircraftDocument.class);
        return Optional.ofNullable(doc).map(this::toDomain);
    }

    @Override
    public List<Aircraft> findAll() {
        return mongoTemplate.findAll(AircraftDocument.class)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByRegistration(String registration) {
        Query query = new Query(Criteria.where("registration").is(registration));
        return mongoTemplate.exists(query, AircraftDocument.class);
    }

    @Override
    public void saveAll(List<Aircraft> aircrafts) {
        List<AircraftDocument> docs = aircrafts.stream()
                .map(a -> {
                    AircraftDocument doc = toDocument(a);
                    if (doc.getId() == null) doc.setId(UUID.randomUUID());
                    return doc;
                })
                .toList();

        mongoTemplate.insertAll(docs);
    }

    // --- MAPPERY ---

    private AircraftDocument toDocument(Aircraft aircraft) {
        if (aircraft == null) return null;
        return AircraftDocument.builder()
                .id(aircraft.getId())
                .model(aircraft.getModel())
                .productionYear(aircraft.getProductionYear())
                .registration(aircraft.getRegistration())
                .totalSeats(aircraft.getTotalSeats())
                .seatClassRanges(aircraft.getSeatClassRanges()) // Kopiujemy mapę 1:1
                .build();
    }

    private Aircraft toDomain(AircraftDocument doc) {
        if (doc == null) return null;
        return Aircraft.builder()
                .id(doc.getId())
                .model(doc.getModel())
                .productionYear(doc.getProductionYear())
                .registration(doc.getRegistration())
                .totalSeats(doc.getTotalSeats())
                .seatClassRanges(doc.getSeatClassRanges()) // Kopiujemy mapę 1:1
                .build();
    }
}
