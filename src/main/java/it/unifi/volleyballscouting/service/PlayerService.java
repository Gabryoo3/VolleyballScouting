package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.PlayerFormDto;
import it.unifi.volleyballscouting.model.Player;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PlayerService {

    public record AssignmentResult(Long playerId, boolean hasNumberConflict, Integer number){}

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository repo){
        this.playerRepository = repo;
    }

    public List<Player> findAll(){
        return playerRepository.findAll();
    }

    public Player findById(Long id){
        return playerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Giocatore specificato non trovato"));
    }

    public List<Player> findByTeamId(Long teamId){
        return playerRepository.findByTeamId(teamId);
    }

    public List<Player> findBySurname(String surname){
        return playerRepository.findBySurnameContainingIgnoreCase(surname);
    }

    public List<Player> findByNumber(int num){
        return playerRepository.findByNumber(num);
    }

    public List<Player> findByRole(String role){
        return playerRepository.findByRole(role);
    }

    public List<Player> findByTeamIdAndSurname(Long teamId, String surname){
        return playerRepository.findByTeamIdAndSurnameContainingIgnoreCase(teamId, surname);
    }

    public List<Player> findByTeamIdAndRole(Long teamId, String role){
        return playerRepository.findByTeamIdAndRole(teamId, role);
    }

    public Player findByTeamIdAndNumber(Long teamId, int num){
        return playerRepository.findByTeamIdAndNumber(teamId, num);
    }

    public List<Player> findByTeamIdIsNull(){
        return playerRepository.findByTeamIdIsNull();
    }

    public List<Player> findByTeamIdIsNullAndRole(String role){
        return playerRepository.findByTeamIdIsNullAndRole(role);
    }

    public List<Integer> listTeamPlayersNumbers(Long teamId){
        return playerRepository.findNumberByTeamId(teamId);
    }

    @Transactional
    public Player save(PlayerFormDto form, Team team){
        Player player = new Player(form.surname(), form.name(), form.username(), form.birthdate(), form.number(), form.role());
        player.setTeam(team);
        return playerRepository.save(player);
    }

    @Transactional
    public AssignmentResult addPlayerToTeam(Long playerId, Team t){
        Player p = findById(playerId);
        p.setTeam(t);
        boolean conflict = isNumberAlreadyTaken(t.getId(), p.getNumber());
        p.setTeam(t);
        return new AssignmentResult(playerId, conflict, p.getNumber());
    }
    public Player updatePlayer(Long playerId, PlayerFormDto form){
        Player p = findById(playerId);
    }

    public boolean isNumberAlreadyTaken(Long teamId, int number){
        List<Integer> teamNumbers = listTeamPlayersNumbers(teamId);
        return teamNumbers.contains(number);
    }
}

