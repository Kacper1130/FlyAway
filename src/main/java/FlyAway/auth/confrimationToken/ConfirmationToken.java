package FlyAway.auth.confrimationToken;

import FlyAway.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String token;
    private LocalDateTime expiresAt;

    @ManyToOne
    private User user;

}
