package FlyAway.flight.dao;

import FlyAway.flight.Flight;
import FlyAway.flight.FlightDocument;
import FlyAway.flight.aircraft.Aircraft;
import FlyAway.flight.aircraft.AircraftDocument;
import FlyAway.flight.airport.Airport;
import FlyAway.flight.airport.AirportDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public class MongoFlightAdapter implements FlightRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Flight save(Flight flight) {
        FlightDocument doc = toDocument(flight);

        if (doc.getId() == null) {
            doc.setId(UUID.randomUUID());
        }

        mongoTemplate.save(doc);

        // Zwracamy obiekt domenowy (musimy go odtworzyć, w tym dociągnąć samolot)
        // Optymalizacja: można po prostu zaktualizować ID w obiekcie wejściowym i go zwrócić,
        // ale dla czystości robimy toDomain.
        return toDomain(doc);
    }

    @Override
    public List<Flight> saveAll(List<Flight> flights) {
        List<FlightDocument> docs = flights.stream()
                .map(f -> {
                    FlightDocument doc = toDocument(f);
                    if (doc.getId() == null) doc.setId(UUID.randomUUID());
                    return doc;
                })
                .toList();

        mongoTemplate.insertAll(docs);

        // Zwracamy listę domenową (wymaga ponownego mapowania)
        return docs.stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Flight> findById(UUID id) {
        FlightDocument doc = mongoTemplate.findById(id, FlightDocument.class);
        return Optional.ofNullable(doc).map(this::toDomain);
    }

    @Override
    public List<Flight> findAll() {
        return mongoTemplate.findAll(FlightDocument.class)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Flight> findFutureFlights(Pageable pageable, LocalDateTime now) {
        Query query = new Query();
        query.addCriteria(Criteria.where("departureDate").gt(now)); // Data > teraz
        query.with(pageable);

        List<FlightDocument> docs = mongoTemplate.find(query, FlightDocument.class);
        long count = mongoTemplate.count(query.skip(-1).limit(-1), FlightDocument.class);

        return new PageImpl<>(docs.stream().map(this::toDomain).toList(), pageable, count);
    }

    @Override
    public Page<Flight> findByFilters(Map<String, Object> filters, Pageable pageable) {
        Query query = new Query();

        // --- TŁUMACZENIE FILTRÓW (Map -> Mongo Criteria) ---
        // To jest odpowiednik Twojego FlightSpecification z SQL

        // 1. Miasto wylotu (szukamy w zagnieżdżonym obiekcie Airport!)
        if (filters.get("departureCity") != null) {
            String city = (String) filters.get("departureCity");
            // "departureAirport.city" - kropka wchodzi w głąb JSON-a
            query.addCriteria(Criteria.where("departureAirport.city").is(city));
        }

        // 2. Miasto przylotu
        if (filters.get("arrivalCity") != null) {
            String city = (String) filters.get("arrivalCity");
            query.addCriteria(Criteria.where("arrivalAirport.city").is(city));
        }

        // 3. Data wylotu (zakres dnia)
        if (filters.get("departureDate") != null) {
            String dateStr = (String) filters.get("departureDate");
            // Zakładam format YYYY-MM-DD
            LocalDateTime startOfDay = LocalDateTime.parse(dateStr + "T00:00:00");
            LocalDateTime endOfDay = LocalDateTime.parse(dateStr + "T23:59:59");

            query.addCriteria(Criteria.where("departureDate").gte(startOfDay).lte(endOfDay));
        }

        // ... dodaj inne filtry jeśli potrzebujesz ...

        query.with(pageable);
        List<FlightDocument> docs = mongoTemplate.find(query, FlightDocument.class);
        long count = mongoTemplate.count(query.skip(-1).limit(-1), FlightDocument.class);

        return new PageImpl<>(docs.stream().map(this::toDomain).toList(), pageable, count);
    }

    // --- MAPPERY ---

    private FlightDocument toDocument(Flight flight) {
        if (flight == null) return null;
        return FlightDocument.builder()
                .id(flight.getId())
                .departureDate(flight.getDepartureDate())
                .arrivalDate(flight.getArrivalDate())
                .cabinClassPrices(flight.getCabinClassPrices())
                .aircraftId(flight.getAircraft() != null ? flight.getAircraft().getId() : null)
                // Mapujemy Lotniska na AirportDocument (zagnieżdżenie)
                .departureAirport(mapAirportToDoc(flight.getDepartureAirport()))
                .arrivalAirport(mapAirportToDoc(flight.getArrivalAirport()))
                .build();
    }

    private Flight toDomain(FlightDocument doc) {
        if (doc == null) return null;

        // Dociąganie Samolotu (Lazy loading simulation)
        // Musimy pobrać dokument samolotu z innej kolekcji, żeby odtworzyć obiekt Flight
        Aircraft aircraft = null;
        if (doc.getAircraftId() != null) {
            // 1. Adapter pyta bazę: "Daj mi dokument samolotu o tym ID"
            AircraftDocument aircraftDoc = mongoTemplate.findById(doc.getAircraftId(), AircraftDocument.class);

            // 2. Jeśli znalazł, zamienia go na obiekt domenowy Aircraft
            if (aircraftDoc != null) {
                aircraft = mapAircraftDocToEntity(aircraftDoc);
            }
        }

        return Flight.builder()
                .id(doc.getId())
                .departureDate(doc.getDepartureDate())
                .arrivalDate(doc.getArrivalDate())
                .cabinClassPrices(doc.getCabinClassPrices())
                .aircraft(aircraft)
                // Odtwarzamy lotniska z zagnieżdżonych dokumentów
                .departureAirport(mapDocToAirport(doc.getDepartureAirport()))
                .arrivalAirport(mapDocToAirport(doc.getArrivalAirport()))
                .reservations(new ArrayList<>()) // Rezerwacje są w innej kolekcji, tu pusta lista
                .build();
    }

    // --- POMOCNICZE MAPPERY DLA LOTNISK I SAMOLOTÓW ---

    private AirportDocument mapAirportToDoc(Airport a) {
        if (a == null) return null;
        // Uwaga: Country też musi być zmapowane, jeśli AirportDocument je zawiera
        return AirportDocument.builder()
                .id(a.getId())
                .name(a.getName())
                .city(a.getCity())
                .IATACode(a.getIATACode())
                .enabled(a.isEnabled())
                .country(a.getCountry()) // Zakładam, że AirportDocument ma pole Country
                .build();
    }

    private Airport mapDocToAirport(AirportDocument d) {
        if (d == null) return null;
        return Airport.builder()
                .id(d.getId())
                .name(d.getName())
                .city(d.getCity())
                .IATACode(d.getIATACode())
                .enabled(d.isEnabled())
                .country(d.getCountry())
                .build();
    }

    private Aircraft mapAircraftDocToEntity(AircraftDocument d) {
        if (d == null) return null;
        return Aircraft.builder()
                .id(d.getId())
                .model(d.getModel())
                .registration(d.getRegistration())
                .totalSeats(d.getTotalSeats())
                .seatClassRanges(d.getSeatClassRanges())
                .productionYear(d.getProductionYear())
                .build();
    }
}
