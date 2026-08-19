package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.repository.CoachRepository;
import it.unifi.volleyballscouting.repository.PlayerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserDetailService implements UserDetailsService {

    private final CoachRepository coachRepository;
    private final PlayerRepository playerRepository;

    public AppUserDetailService(CoachRepository cr, PlayerRepository pr){
        this.coachRepository = cr;
        this.playerRepository = pr;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<Coach> coach = coachRepository.findByUsername(username);
        if (coach.isPresent())
            return coach.get();
        return playerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
    }

}
