package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.UserAccount;
import it.unifi.volleyballscouting.model.Admin;
import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Player;
import it.unifi.volleyballscouting.model.Referee;
import it.unifi.volleyballscouting.repository.AdminRepository;
import it.unifi.volleyballscouting.repository.CoachRepository;
import it.unifi.volleyballscouting.repository.PlayerRepository;
import it.unifi.volleyballscouting.repository.RefereeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAccountService {

    private final AdminRepository adminRepository;
    private final CoachRepository coachRepository;
    private final PlayerRepository playerRepository;
    private final RefereeRepository refereeRepository;

    public UserAccountService(AdminRepository adminRepository, CoachRepository coachRepository, PlayerRepository playerRepository, RefereeRepository refereeRepository) {
        this.adminRepository = adminRepository;
        this.coachRepository = coachRepository;
        this.playerRepository = playerRepository;
        this.refereeRepository = refereeRepository;
    }

    public Optional<UserAccount> findByUsername(String username){
        if (username == null || username.isBlank()) return Optional.empty();

        Optional<Admin> adminOpt = adminRepository.findByUsernameIgnoreCase(username);
        if(adminOpt.isPresent()){
            Admin a = adminOpt.get();
            return Optional.of(
                    new UserAccount(
                    a.getId(),
                    a.getUsername(),
                    a.getPassword(),
                    "ADMIN"
                    )
            );
        }
        Optional<Coach> coachOpt = coachRepository.findByUsernameIgnoreCase(username);
        if (coachOpt.isPresent()){
            Coach c = coachOpt.get();
            return Optional.of(
                    new UserAccount(
                            c.getId(),
                            c.getUsername(),
                            c.getPassword(),
                            "COACH"
                    )
            );
        }
        Optional<Referee> refereeOpt = refereeRepository.findByUsernameIgnoreCase(username);
        if(refereeOpt.isPresent()){
            Referee r = refereeOpt.get();
            return Optional.of(
                    new UserAccount(
                            r.getId(),
                            r.getUsername(),
                            r.getPassword(),
                            "REFEREE"
                    )
            );
        }
        Optional<Player> playerOpt = playerRepository.findByUsernameIgnoreCase(username);
        if (playerOpt.isPresent()){
            Player p = playerOpt.get();
            return Optional.of(
                    new UserAccount(
                            p.getId(),
                            p.getUsername(),
                            p.getPassword(),
                            "PLAYER"
                    )
            );
        }
        return Optional.empty();
    }

    public boolean isUsernameTaken(String username){
        return findByUsername(username).isPresent();
    }

    }

