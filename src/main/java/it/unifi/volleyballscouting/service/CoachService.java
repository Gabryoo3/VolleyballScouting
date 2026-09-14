package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.CoachFormDto;
import it.unifi.volleyballscouting.dto.TeamStatsDTO;
import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.CoachRepository;
import it.unifi.volleyballscouting.repository.TeamRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoachService {
    private final CoachRepository coachRepository;

    private final PasswordEncoder passwordEncoder;

    public CoachService(CoachRepository repo, PasswordEncoder ps){
        this.coachRepository = repo;
        this.passwordEncoder = ps;
    }

    public List<Coach> findAll() { return coachRepository.findAll();}


    public Coach findById(UUID id){
        return coachRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Allenatore non trovato"));
    }

    public Team getCoachTeam(UUID coachId){
        Coach c = coachRepository.findById(coachId).orElseThrow(() -> new IllegalArgumentException("Coach non trovato"));
        return c.getTeam();
    }

    @Transactional
    public void save(CoachFormDto form){
        String encodedPassword = passwordEncoder.encode(form.password());
        Coach coach = new Coach(form.name(), form.surname(), form.username(), encodedPassword, form.birthdate());
        coach.setPhone(form.phone());
        coach.setEmail(form.email());
        coachRepository.save(coach);
    }

    @Transactional
    public void assignTeam(UUID coachId, Team t){
        Coach c = findById(coachId);
        c.assignTeam(t);
    }

    @Transactional
    public void updateCoach(UUID coachId, CoachFormDto form){
        Coach c = findById(coachId);
        c.updateFromDto(form);
    }
}
