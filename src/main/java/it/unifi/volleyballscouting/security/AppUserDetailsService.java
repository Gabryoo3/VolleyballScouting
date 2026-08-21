package it.unifi.volleyballscouting.security;

import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Player;
import it.unifi.volleyballscouting.repository.CoachRepository;
import it.unifi.volleyballscouting.repository.PlayerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final CoachRepository coachRepository;
    private final PlayerRepository playerRepository;

    public AppUserDetailsService(CoachRepository cr, PlayerRepository pr){
        this.coachRepository = cr;
        this.playerRepository = pr;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Coach> coachOpt = coachRepository.findByUsername(username);
        if (coachOpt.isPresent()){
            Coach c = coachOpt.get();
            return new AppUserDetails(
                    c.getId(),
                    c.getUsername(),
                    c.getPassword(),
                    "COACH"
            );
        }
        Optional<Player> playerOpt = playerRepository.findByUsername(username);
        if (playerOpt.isPresent()){
            Player p = playerOpt.get();
            return new AppUserDetails(
                    p.getId(),
                    p.getUsername(),
                    p.getPassword(),
                    "PLAYER"
            );
        }
        throw new UsernameNotFoundException("Utente non trovato con username: " + username);
    }
}
