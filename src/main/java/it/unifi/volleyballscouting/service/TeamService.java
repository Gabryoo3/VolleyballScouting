package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    public final TeamRepository teamRepo;

    public TeamService(TeamRepository repo){ this.teamRepo = repo;}

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
}
