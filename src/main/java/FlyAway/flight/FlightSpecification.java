package FlyAway.flight;

import FlyAway.exception.UnsupportedFilterKeyException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FlightSpecification {

    public static Specification<Flight> filterFlights(Map<String, Object> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            filters.forEach((key, value) -> {
                switch (key) {
                    case "departureCity":
                        predicates.add(criteriaBuilder.equal(root.get("departureAirport").get("city"), value));
                        break;
                    case "arrivalCity":
                        predicates.add(criteriaBuilder.equal(root.get("arrivalAirport").get("city"), value));
                        break;
                    case "departureDate":
                        LocalDate departureDate = parseToLocalDate(value);
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("departureDate"), departureDate));
                        break;
                    case "arrivalDate":
                        LocalDate arrivalDate = parseToLocalDate(value);
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("arrivalDate"), arrivalDate));
                        break;
                    case "arrivalCountry":
                        predicates.add(criteriaBuilder.equal(root.get("arrivalAirport").get("country").get("name"), value));
                        break;
                    default:
                        throw new UnsupportedFilterKeyException(key);
                }
            });
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static LocalDate parseToLocalDate(Object value) {
        if (value instanceof String s) {
            return LocalDate.parse(s);
        } else if (value instanceof LocalDate localDate) {
            return localDate;
        } else {
            throw new IllegalArgumentException("Unsupported date value type: " + value.getClass());
        }
    }

}
