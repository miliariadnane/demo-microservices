package dev.nano.application;

import dev.nano.apikey.ApiKeyEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;

@Entity
@Table(name = "applications")
@Data @SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class ApplicationEntity {
    @Id
    @SequenceGenerator(
            name = "application_sequence",
            sequenceName = "application_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "application_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationName applicationName;
    private boolean enabled;
    private boolean approved;
    private boolean revoked;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "api_key_id",
            referencedColumnName = "id"
    )
    private ApiKeyEntity apiKey;
}
