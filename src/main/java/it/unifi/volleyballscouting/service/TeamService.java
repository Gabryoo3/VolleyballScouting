package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.TeamFormDto;
import it.unifi.volleyballscouting.model.Address;
import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional(readOnly = true)
public class TeamService {
    private final TeamRepository teamRepo;
    private final CoachService coachService;

    public TeamService(TeamRepository repo, CoachService coachService){ this.teamRepo = repo;
        this.coachService = coachService;
    }

    public List<Team> findAll(){ return teamRepo.findAll();}

    public Team findById(Long id){
        return teamRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Team specificato non trovato"));
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
    @Transactional
    public Team save (TeamFormDto form, Coach coach){
        Address ad = new Address(
                form.address().street(),
                form.address().city(),
                form.address().zipCode()
        );
        Team team = new Team (form.name(), ad, coach);
        return teamRepo.save(team);
    }
    @Transactional
    public void updateTeam(Long teamId, TeamFormDto form){
        Team t = findById(teamId);
        t.updateFromDto(form);
    }

}
