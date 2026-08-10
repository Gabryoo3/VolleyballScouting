package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByNameContainingIgnoreCase(String teamName);
    List<Team> findByAddressCityContainingIgnoreCase(String city);
}