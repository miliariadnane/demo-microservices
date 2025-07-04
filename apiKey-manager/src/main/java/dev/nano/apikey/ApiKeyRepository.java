package dev.nano.apikey;

import dev.nano.application.ApplicationName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {
    @Query("""
        SELECT ak FROM ApiKeyEntity ak
        INNER JOIN ApplicationEntity ap
        ON ak.id = ap.apiKey.id
        WHERE ak.key = :key
        AND ap.applicationName = :applicationName
    """)
    Optional<ApiKeyEntity> findByKeyAndApplicationName(@Param("key") String key, @Param("applicationName") ApplicationName applicationName);

    @Query("""
        SELECT
        CASE WHEN COUNT(ak) > 0
          THEN TRUE
          ELSE FALSE
        END
        FROM ApiKeyEntity ak
        WHERE ak.key = :key
    """)
    boolean doesKeyExists(String key);

    @Query("SELECT ak FROM ApiKeyEntity ak WHERE ak.key = :key")
    Optional<ApiKeyEntity> findByKey(String key);
}
