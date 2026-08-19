package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.Player;
import it.unifi.volleyballscouting.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    //Global searches
    List<Player> findBySurnameContainingIgnoreCase(String surname);
    List<Player> findByNumber(int number);
    List<Player> findByRole(String role);
    List<Player> findByTeamId (Long teamId);
    //Local searches
    List<Player> findByTeamIdAndSurnameContainingIgnoreCase(Long teamID, String surname);
    List<Player> findByTeamIdAndRole(Long teamID, String role);
    Player findByTeamIdAndNumber(Long teamID, int number);
    List<Player> findByTeamIdIsNull();
    List<Player> findByTeamIdIsNullAndRole(String role);
    Optional<Player> findByUsername(String username);
    boolean existsByTeamIdAndNumber(Long teamId, Integer number);
    boolean existsByTeamIdAndNumberAndIdNot(Long teamId, Integer number, Long playerId);

}
