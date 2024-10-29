package gajudama.javematch.model;

import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario user1;  // First user in the match

    @ManyToOne
    private Usuario user2;  // Second user in the match

    @Temporal(TemporalType.DATE)
    private Date fechaMatch;  // Match date

    private Boolean amistad;  // Indicates if the match is a friendship

    @ManyToOne
    private Videollamada videollamada;  // Optional videollamada associated with the match
}
