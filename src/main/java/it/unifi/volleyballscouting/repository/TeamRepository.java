package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
    List<Team> findByNameContainingIgnoreCase(String teamName);
    List<Team> findByAddressCityContainingIgnoreCase(String city);
    List<Team> findByNameContainingIgnoreCaseAndAddressCity(String teamName, String city);
    boolean existsTeamByName(String teamName);
    boolean existsTeamByNameAndIdNot(String teamName, UUID id);
}