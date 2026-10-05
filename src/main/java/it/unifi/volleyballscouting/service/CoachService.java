package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.CoachFormDTO;
import it.unifi.volleyballscouting.dto.CoachStatsDTO;
import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.CoachRepository;
import it.unifi.volleyballscouting.repository.MatchRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
@Transactional(readOnly = true)
@Service
public class CoachService {
    private final CoachRepository coachRepository;
    private final MatchRepository matchRepository;

    private final PasswordEncoder passwordEncoder;

    public CoachService(CoachRepository repo, PasswordEncoder ps, MatchRepository mr){
        this.coachRepository = repo;
        this.passwordEncoder = ps;
        this.matchRepository = mr;
    }

    public List<Coach> findAll() { return coachRepository.findAll();}


    public Coach findById(UUID id){
        return coachRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Allenatore non trovato"));
    }
    public Team getCoachTeam(UUID coachId){
        Coach c = coachRepository.findById(coachId).orElseThrow(() -> new IllegalArgumentException("Allenatore non trovato"));
        if (c.getTeam() == null)
            throw new NullPointerException("L'allenatore non ha ancora un team");
        return c.getTeam();
    }

    public CoachStatsDTO getCoachStats(Coach coach){
        Team t = coach.getTeam();
        if (t == null) return CoachStatsDTO.empty(coach.getId());
        long wins = matchRepository.countWins(t);
        long loses = matchRepository.countLoses(t);
        return new CoachStatsDTO(coach.getId(), wins, loses, wins+loses);
    }

    @Transactional
    public void save(CoachFormDTO form){
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
    public void updateCoach(UUID coachId, CoachFormDTO form){
        Coach c = findById(coachId);
        c.updateFromDto(form);
    }
}
