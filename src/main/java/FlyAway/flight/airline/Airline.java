//package FlyAway.flight.airline;
//import FlyAway.flight.aircraft.Aircraft;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//import lombok.*;
//
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "airlines")
//public class Airline {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID id;
//
//    @NotBlank
//    private String name;
//
//    @NotBlank
//    private String IATACode;
//
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<Aircraft> fleet;
//
//    @ElementCollection
//    @CollectionTable(name = "airline_class_prices", joinColumns = @JoinColumn(name = "airline_id"))
//    @MapKeyColumn(name = "class_type")
//    @Column(name = "price")
//    private Map<String, Long> pricesByClass;
//
//    public Long getPriceForClass(String classType) {
//        return pricesByClass.getOrDefault(classType, 0L);
//    }
//
//    public void addAircraftToFleet(Aircraft aircraft) {
//        this.fleet.add(aircraft);
//    }
//
//    public void removeAircraftFromFleet(Aircraft aircraft) {
//        this.fleet.remove(aircraft);
//    }
//}
