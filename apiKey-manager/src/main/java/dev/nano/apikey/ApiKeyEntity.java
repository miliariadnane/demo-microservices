package dev.nano.apikey;

import dev.nano.application.ApplicationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "api_keys")
@Data @SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class ApiKeyEntity {
    @Id
    @SequenceGenerator(
            name = "api_key_sequence",
            sequenceName = "api_key_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "api_key_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(unique = true, nullable = false)
    private String key;

    @Column(nullable = false, unique = true)
    private String client;

    private String description;

    private LocalDateTime createdDate;

    private LocalDateTime expirationDate;

    private boolean enabled;

    private boolean neverExpires;

    private boolean approved;

    private boolean revoked;

    @OneToMany(mappedBy = "apiKey")
    private List<ApplicationEntity> applications;
}
