package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.PlayerFormDTO;
import it.unifi.volleyballscouting.model.Player;
import it.unifi.volleyballscouting.model.PlayerRole;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.PlayerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PlayerService {

    private final PasswordEncoder passwordEncoder;
    private final TeamService teamService;
    private final UserAccountService userAccountService;

    public record AssignmentResult(UUID playerId, boolean hasNumberConflict, Integer number){}

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository repo, PasswordEncoder passwordEncoder, TeamService teamService, UserAccountService userAccountService){
        this.playerRepository = repo;
        this.passwordEncoder = passwordEncoder;
        this.teamService = teamService;
        this.userAccountService = userAccountService;
    }

    public List<Player> findAll(){
        return playerRepository.findAll();
    }

    public Player findById(UUID id){
        return playerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Giocatore specificato non trovato"));
    }

    public List<Player> findByTeamId(UUID teamId){
        return playerRepository.findByTeamId(teamId);
    }

    public List<Player> findBySurname(String surname){
        return playerRepository.findBySurnameContainingIgnoreCase(surname);
    }

    public List<Player> findByNumber(int num){
        return playerRepository.findByNumber(num);
    }

    public List<Player> findByRole(PlayerRole role){
        return playerRepository.findByRole(role);
    }

    public List<Player> findByTeamIdAndSurname(UUID teamId, String surname){
        return playerRepository.findByTeamIdAndSurnameContainingIgnoreCase(teamId, surname);
    }

    public List<Player> findByTeamIdAndRole(UUID teamId, PlayerRole role){
        return playerRepository.findByTeamIdAndRole(teamId, role);
    }

    public Player findByTeamIdAndNumber(UUID teamId, int num){
        return playerRepository.findByTeamIdAndNumber(teamId, num);
    }

    public List<Player> findByTeamIdIsNull(){
        return playerRepository.findByTeamIdIsNull();
    }

    public List<Player> findByTeamIdIsNullAndRole(PlayerRole role){
        return playerRepository.findByTeamIdIsNullAndRole(role);
    }

    @Transactional
    public String save(PlayerFormDTO form, UUID teamId){
        if (userAccountService.isUsernameTaken(form.username()))
            throw new IllegalArgumentException("Lo username inserito è già in uso");
        if (isNumberAlreadyTaken(teamId, form.number(), null))
            throw new IllegalArgumentException("Il numero inserito è già assegnato nel team"); //DA SEGNARE COME VOLUTO E NON COME DUPLICATO
        Team team = teamService.findById(teamId);
        String tempPassword = UUID.randomUUID().toString().substring(0,8);
        String encodedPassword = passwordEncoder.encode(tempPassword);

        Player player = new Player(form.surname(), form.name(), form.username(), encodedPassword, form.birthdate(), form.number(), form.role());
        player.setTeam(team);
        player.setPhone(form.phone());
        player.setEmail(form.email());
        playerRepository.save(player);
        return tempPassword;
    }

    @Transactional
    public AssignmentResult addPlayerToTeam(UUID playerId, UUID teamId){
        Player p = findById(playerId);
        Team t = teamService.findById(teamId);
        boolean conflict = isNumberAlreadyTaken(teamId, p.getNumber(), playerId);
        p.setTeam(t);
        return new AssignmentResult(playerId, conflict, p.getNumber());
    }

    @Transactional
    public void updatePlayer(UUID playerId, PlayerFormDTO form){
        Player p = findById(playerId);
        p.updateFromDto(form);
    }

    public boolean isNumberAlreadyTaken(UUID teamId, Integer number, UUID playerId){
        if(teamId == null || number == null) {
            return false;
        }
        if (playerId == null){
            return playerRepository.existsByTeamIdAndNumber(teamId, number);
        }
        return playerRepository.existsByTeamIdAndNumberAndIdNot(teamId, number, playerId);
    }
}

