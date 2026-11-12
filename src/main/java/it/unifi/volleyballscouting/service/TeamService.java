package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.details.TeamDetailsDTO;
import it.unifi.volleyballscouting.dto.forms.TeamFormDTO;
import it.unifi.volleyballscouting.exception.BusinessRuleException;
import it.unifi.volleyballscouting.exception.ResourceNotFoundException;
import it.unifi.volleyballscouting.model.Address;
import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.TeamRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TeamService {
    private final TeamRepository teamRepo;
    private final CoachService coachService;

    public TeamService(TeamRepository repo, CoachService coachService){ this.teamRepo = repo;
        this.coachService = coachService;
    }

    public List<Team> findAll(){ return teamRepo.findAll();}

    public Team findById(UUID id){
        return teamRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Team specificato non trovato"));
    }

    public List<Team> findByName(String name){
        return teamRepo.findByNameContainingIgnoreCase(name);
    }

    public List<Team> findByCity(String city){
        return teamRepo.findByAddressCityContainingIgnoreCase(city);
    }

    public List<Team> findByNameAndCity(String name, String city){
        return teamRepo.findByNameContainingIgnoreCaseAndAddressCity(name, city);
    }

    public Team findOwnedBy(UUID teamId, UUID coachId){
        Team t = findById(teamId);
        if(t.getCoach() == null || !t.getCoach().getId().equals(coachId)){
            throw new AccessDeniedException("Non puoi modificare questo team");
        }
        return t;
    }

	public TeamDetailsDTO getDetails (UUID teamId, UUID viewerId){
		Team t = findById(teamId);
		boolean editable = t.getCoach() != null && t.getCoach().getId().equals(viewerId);
		return TeamDetailsDTO.showDetails(t, editable);
	}
    @Transactional
    public Team save (TeamFormDTO form, UUID coachId){
        if(teamRepo.existsTeamByName(form.name())){
            throw new BusinessRuleException("Questo nome esiste già tra i team");
        }
        Coach coach = coachService.findById(coachId);
        Address ad = new Address(
                form.address().street(),
                form.address().city(),
                form.address().zipCode()
        );
        Team team = new Team (form.name(), ad, coach);
        Team saved = teamRepo.save(team);
        coach.setTeam(saved);
        return saved;
    }
    @Transactional
    public void updateTeam(UUID teamId, UUID coachId, TeamFormDTO form){
        Team own = coachService.getCoachTeam(teamId);
        if (own == null || !own.getId().equals(teamId))
            throw new AccessDeniedException("Non puoi modificare questo team");
        Team t = findById(teamId);
        if (teamRepo.existsTeamByNameAndIdNot(form.name(), teamId)){
            throw new BusinessRuleException("Il nome del team inserito esiste già");
        }
        t.updateFromDto(form);
    }

}
