package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.Player;
import it.unifi.volleyballscouting.model.PlayerRole;
import it.unifi.volleyballscouting.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {
    //Global searches
    List<Player> findBySurnameContainingIgnoreCase(String surname);
    List<Player> findByNumber(int number);
    List<Player> findByRole(PlayerRole role);
    List<Player> findByTeamId (UUID teamId);
    //Local searches
    List<Player> findByTeamIdAndSurnameContainingIgnoreCase(UUID teamID, String surname);
    List<Player> findByTeamIdAndRole(UUID teamID, PlayerRole role);
    Player findByTeamIdAndNumber(UUID teamID, int number);
    List<Player> findByTeamIdIsNull();
    List<Player> findByTeamIdIsNullAndRole(PlayerRole role);
    Optional<Player> findByUsername(String username);
    boolean existsByTeamIdAndNumber(UUID teamId, Integer number);
    boolean existsByTeamIdAndNumberAndIdNot(UUID teamId, Integer number, UUID playerId);

}
