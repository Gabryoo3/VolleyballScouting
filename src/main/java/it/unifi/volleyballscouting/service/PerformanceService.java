package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.PlayerStatsDTO;
import it.unifi.volleyballscouting.dto.TeamStatsDTO;
import it.unifi.volleyballscouting.model.*;
import it.unifi.volleyballscouting.query.PerformanceQuery;
import it.unifi.volleyballscouting.repository.PerformanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerformanceService {

    private final PerformanceRepository perfRepo;

    public PerformanceService(PerformanceRepository repo){
        this.perfRepo = repo;
    }

    public List<Performance> findFiltered(PerformanceQuery q){
        return perfRepo.findAll(q.toSpecification());

    }

    //Aggregated Queries

    public PlayerStatsDTO getPlayerCareerStats(Player player){
        return perfRepo.getPlayerCareerStats(player).orElseGet(() -> PlayerStatsDTO.empty(player.getId()));
    }

    public PlayerStatsDTO getPlayerMatchStats(Player player, Match match){
        return perfRepo.getPlayerMatchStats(player, match).orElseGet(() -> PlayerStatsDTO.empty(player.getId()));
    }

    public Performance getPlayerSetPerformance(Player player, Set set){
        return perfRepo.findByPlayerIdAndSetId(player.getId(), set.getId()).orElseThrow(() -> new EntityNotFoundException("Il giocatore non ha giocato nel set selezionato"));
    }

    public TeamStatsDTO getTeamMatchStats(Team team, Match match){
        return perfRepo.getTeamMatchStats(team , match).orElseGet(() -> TeamStatsDTO.empty(team.getId()));
    }

    public TeamStatsDTO getTeamStats(Team team){
        return perfRepo.getTeamGlobalStats(team).orElseGet(() -> TeamStatsDTO.empty(team.getId()));
    }

    public List<TeamStatsDTO> getBothTeamMatchStats(Match match){
        return perfRepo.getBothTeamsStatsForMatch(match);
    }
}

