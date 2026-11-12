package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.forms.CoachFormDTO;
import it.unifi.volleyballscouting.exception.BusinessRuleException;
import it.unifi.volleyballscouting.exception.ResourceNotFoundException;
import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.CoachRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
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
        return coachRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Allenatore non trovato"));
    }
    public Team getCoachTeam(UUID coachId){
        return findById(coachId).getTeam();
    }

    public Coach findOwnedBy(UUID coachId, UUID requesterId){
        if(!coachId.equals(requesterId))
            throw new AccessDeniedException("Non puoi modificare un altro allenatore");
        return findById(coachId);

    }

    @Transactional
    public Coach save(CoachFormDTO form){
        if (userAccountService.isUsernameTaken(form.username())){
            throw new BusinessRuleException("Lo username inserito è già in uso");
        }
        if (Period.between(form.birthdate(), LocalDate.now()).getYears() < 18){
            throw new BusinessRuleException("L'allenatore deve essere maggiorenne");
        }
        Coach c = new Coach(form.name(), form.surname(), form.username(), passwordEncoder.encode(form.password()), form.birthdate());
        c.setPhone(form.phone());
        c.setEmail(form.email());
        return coachRepository.save(c);
    }

    @Transactional
    public void updateCoach(UUID coachId, UUID requesterId, CoachFormDTO form){
        Coach c = findOwnedBy(coachId, requesterId);
        if (!c.getUsername().equalsIgnoreCase(form.username()) && userAccountService.isUsernameTaken(form.username())){
            throw new BusinessRuleException("Lo username inserito è già in uso");
        }
        c.updateFromDto(form);
    }
}
