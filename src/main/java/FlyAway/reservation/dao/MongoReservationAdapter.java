package FlyAway.reservation.dao;

import FlyAway.flight.Flight;
import FlyAway.flight.FlightDocument;
import FlyAway.flight.aircraft.Aircraft;
import FlyAway.flight.aircraft.AircraftDocument;
import FlyAway.flight.airport.Airport;
import FlyAway.flight.airport.AirportDocument;
import FlyAway.reservation.Reservation;
import FlyAway.reservation.ReservationDocument;
import FlyAway.reservation.ReservationStatus;
import FlyAway.user.User;
import FlyAway.user.dao.UserDocument;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public class MongoReservationAdapter implements ReservationRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Reservation save(Reservation reservation) {
        ReservationDocument doc = toDocument(reservation);

        if (doc.getId() == null) {
            doc.setId(UUID.randomUUID());
        }

        // OPTYMALIZACJA: Kopiujemy datę przylotu z obiektu Flight do dokumentu Rezerwacji
        if (reservation.getFlight() != null) {
            doc.setFlightArrivalDate(reservation.getFlight().getArrivalDate());
        }

        mongoTemplate.save(doc);

        // Aktualizujemy ID w obiekcie domenowym i zwracamy go
        // (Nie robimy pełnego toDomain, żeby nie tracić czasu na ponowne pobieranie Usera/Lotu, które już mamy)
        reservation.setId(doc.getId());
        return reservation;
    }

    @Override
    public List<Reservation> saveAll(List<Reservation> reservations) {
        return reservations.stream().map(this::save).collect(Collectors.toList());
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        ReservationDocument doc = mongoTemplate.findById(id, ReservationDocument.class);
        return Optional.ofNullable(doc).map(this::toDomain);
    }

    @Override
    public List<Reservation> findActiveByUserId(UUID userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.addCriteria(Criteria.where("status").is(ReservationStatus.ACTIVE));

        return mongoTemplate.find(query, ReservationDocument.class)
                .stream().map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findAllByUserId(UUID userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, ReservationDocument.class)
                .stream().map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Reservation> findAll(Pageable pageable) {
        Query query = new Query().with(pageable);
        List<ReservationDocument> docs = mongoTemplate.find(query, ReservationDocument.class);
        long count = mongoTemplate.count(query.skip(-1).limit(-1), ReservationDocument.class);

        List<Reservation> list = docs.stream().map(this::toDomain).toList();
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public List<Reservation> findExpiredReservations(LocalDateTime now) {
        // TU WIDAĆ MOC NOSQL:
        // Szukamy po polu 'flightArrivalDate', które jest wewnątrz rezerwacji.
        // W SQL musielibyśmy robić JOIN z tabelą Flights.
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(ReservationStatus.ACTIVE));
        query.addCriteria(Criteria.where("flightArrivalDate").lt(now));

        return mongoTemplate.find(query, ReservationDocument.class)
                .stream().map(this::toDomain)
                .collect(Collectors.toList());
    }

    // --- MAPPERY (Manual JOIN Simulation) ---

    private ReservationDocument toDocument(Reservation r) {
        if (r == null) return null;
        return ReservationDocument.builder()
                .id(r.getId())
                .reservationDate(r.getReservationDate())
                .price(r.getPrice())
                .seatNumber(r.getSeatNumber())
                .cabinClass(r.getCabinClass())
                .status(r.getStatus())
                // Zapisujemy tylko ID
                .userId(r.getClient() != null ? r.getClient().getId() : null) // Używamy getUser() po zmianie nazwy pola!
                .flightId(r.getFlight() != null ? r.getFlight().getId() : null)
                .build();
    }

    private Reservation toDomain(ReservationDocument doc) {
        if (doc == null) return null;

        // SYMULACJA EAGER LOADING:
        // Musimy ręcznie dociągnąć Usera i Lot, bo SQL robił to automatycznie.

        User user = null;
        if (doc.getUserId() != null) {
            UserDocument userDoc = mongoTemplate.findById(doc.getUserId(), UserDocument.class);
            user = mapUserDoc(userDoc);
        }

        Flight flight = null;
        if (doc.getFlightId() != null) {
            FlightDocument flightDoc = mongoTemplate.findById(doc.getFlightId(), FlightDocument.class);
            flight = mapFlightDoc(flightDoc);
        }

        return Reservation.builder()
                .id(doc.getId())
                .reservationDate(doc.getReservationDate())
                .price(doc.getPrice())
                .seatNumber(doc.getSeatNumber())
                .cabinClass(doc.getCabinClass())
                .status(doc.getStatus())
                .client(user)   // Wstawiamy pełny obiekt (zmieniona nazwa z client na user)
                .flight(flight) // Wstawiamy pełny obiekt
                .build();
    }

    // --- POMOCNICZE MAPPERY (Uproszczone odtwarzanie obiektów) ---
    // Potrzebne, żeby z UserDocument zrobić User, a z FlightDocument zrobić Flight

    private User mapUserDoc(UserDocument d) {
        if (d == null) return null;
        return User.builder()
                .id(d.getId())
                .email(d.getEmail())
                .firstname(d.getFirstname())
                .lastname(d.getLastname())
                .role(d.getRole())
                // ... reszta pól ...
                .build();
    }

    private Flight mapFlightDoc(FlightDocument d) {
        if (d == null) return null;

        // Musimy też dociągnąć Aircraft, jeśli Flight go potrzebuje!
        // To pokazuje, że w Mongo relacje są kosztowne (kolejne zapytanie).
        Aircraft aircraft = null;
        if (d.getAircraftId() != null) {
            AircraftDocument acDoc = mongoTemplate.findById(d.getAircraftId(), AircraftDocument.class);
            if(acDoc != null) {
                aircraft = Aircraft.builder()
                        .id(acDoc.getId())
                        .model(acDoc.getModel())
                        .productionYear(acDoc.getProductionYear())
                        .registration(acDoc.getRegistration())
                        .totalSeats(acDoc.getTotalSeats())
                        .seatClassRanges(acDoc.getSeatClassRanges())
                        .build();
            }
        }

        return Flight.builder()
                .id(d.getId())
                .departureDate(d.getDepartureDate())
                .arrivalDate(d.getArrivalDate())
                .aircraft(aircraft)
                // Odtwarzanie lotnisk z zagnieżdżonych dokumentów
                .departureAirport(mapAirportDoc(d.getDepartureAirport()))
                .arrivalAirport(mapAirportDoc(d.getArrivalAirport()))
                .build();
    }

    private Airport mapAirportDoc(AirportDocument d) {
        if (d == null) return null;
        return Airport.builder().id(d.getId()).name(d.getName()).city(d.getCity()).IATACode(d.getIATACode()).build();
    }
}
