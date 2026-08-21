package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CoachRepository extends JpaRepository<Coach, UUID> {
    Optional<Coach> findByUsername(String username);
}