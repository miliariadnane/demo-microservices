package dev.nano.application;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Integer> {
    Optional<ApplicationEntity> findByApplicationName(String applicationName);
}

