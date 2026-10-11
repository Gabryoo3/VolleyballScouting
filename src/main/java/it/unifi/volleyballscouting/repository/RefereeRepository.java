package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.Player;
import it.unifi.volleyballscouting.model.Referee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefereeRepository extends JpaRepository<Referee, UUID> {
    Optional<Referee> findByUsernameIgnoreCase(String username);

    List<Referee> findBySurname(String surname);
    boolean existsByUsernameIgnoreCase(String username);
}