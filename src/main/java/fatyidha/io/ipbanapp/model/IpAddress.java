package fatyidha.io.ipbanapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "_ipAddress")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String ipAddress;
    @Column(nullable = false)
    private boolean isBanned = false;
    private Date bannedDate;
    private String bannedReason;
    private Date createdDate;
    private Date modifiedDate;
    private int attemptsCount;


    @Builder.Default
    @ManyToMany(mappedBy = "ipAddresses", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private Set<User> users = new HashSet<>();

}
