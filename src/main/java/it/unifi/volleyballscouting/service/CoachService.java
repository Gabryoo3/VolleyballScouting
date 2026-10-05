package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.CoachRepository;
import it.unifi.volleyballscouting.security.AppUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class CoachService {

    private final CoachRepository coachRepository;

    public CoachService(CoachRepository repo) {
        this.coachRepository = repo;
    }

    public Coach findById(UUID id) {
        return coachRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Allenatore non trovato"));
    }

    // Metodo tramite ID esplicito
    public Team getCoachTeam(UUID coachId) {
        Coach c = coachRepository.findById(coachId)
                .orElseThrow(() -> new IllegalArgumentException("Coach non trovato"));
        return c.getTeam();
    }

    // Overload senza argomenti (preleva il coach loggato dalla sessione)
    public Team getCoachTeam() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof AppUserDetails userDetails)) {
            return null;
        }
        return getCoachTeam(userDetails.getId());
    }
}