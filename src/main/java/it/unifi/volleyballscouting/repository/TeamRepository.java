package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
    List<Team> findByNameContainingIgnoreCase(String teamName);
    List<Team> findByAddressCityContainingIgnoreCase(String city);
    List<Team> findByNameContainingIgnoreCaseAndAddressCity(String teamName, String city);
}