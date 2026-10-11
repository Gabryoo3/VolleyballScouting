package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.CoachFormDTO;
import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.CoachRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.UUID;
@Transactional(readOnly=true)
@Service
public class CoachService {
    private final CoachRepository coachRepository;
    private final UserAccountService userAccountService;
    private final PasswordEncoder passwordEncoder;

    public CoachService(CoachRepository repo, UserAccountService userAccountService, PasswordEncoder passwordEncoder){
        this.coachRepository = repo;
        this.userAccountService = userAccountService;
        this.passwordEncoder = passwordEncoder;
    }

    public Coach findById(UUID id){
        return coachRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Allenatore non trovato"));
    }
    public Team getCoachTeam(UUID coachId){
        Coach c = coachRepository.findById(coachId).orElseThrow(() -> new IllegalArgumentException("Coach non trovato"));
        return c.getTeam();
    }

    @Transactional
    public Coach save(CoachFormDTO form){
        if (userAccountService.isUsernameTaken(form.username())){
            throw new IllegalArgumentException("Lo username inserito è già in uso");
        }
        Coach c = new Coach(form.name(), form.surname(), form.username(), passwordEncoder.encode(form.password()), form.birthdate());
        c.setPhone(form.phone());
        c.setEmail(form.email());
        return coachRepository.save(c);
    }
}
