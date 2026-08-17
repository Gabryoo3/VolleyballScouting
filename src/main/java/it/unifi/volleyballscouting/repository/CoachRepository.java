package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoachRepository extends JpaRepository<Coach, Long> {
    Optional<Coach> findByUsername(String username);
}