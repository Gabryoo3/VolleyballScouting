package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.CoachRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoachService {
    private final CoachRepository coachRepository;

    public CoachService(CoachRepository repo){
        this.coachRepository = repo;
    }

    public Coach findById(Long id){
        return coachRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Allenatore non trovato"));
    }

    public Coach getLoggedCoach(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return coachRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Coach non trovato"));
    }

    public Team getCoachTeam(){
        return getLoggedCoach().getTeam();
    }

}
