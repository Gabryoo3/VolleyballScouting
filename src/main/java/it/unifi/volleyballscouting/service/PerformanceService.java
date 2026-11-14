package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.stats.PlayerStatsDTO;
import it.unifi.volleyballscouting.dto.stats.TeamStatsDTO;
import it.unifi.volleyballscouting.exception.ResourceNotFoundException;
import it.unifi.volleyballscouting.model.*;
import it.unifi.volleyballscouting.query.PerformanceQuery;
import it.unifi.volleyballscouting.repository.PerformanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@Transactional(readOnly = true)
public class PerformanceService {

    private final PerformanceRepository perfRepo;

	public PerformanceService(PerformanceRepository repo){
        this.perfRepo = repo;
	}

    public List<Performance> findFiltered(PerformanceQuery q){
        return perfRepo.findAll(q.toSpecification());

    }

    //Aggregated Queries

    public PlayerStatsDTO getPlayerCareerStats(UUID playerId){
        return perfRepo.getPlayerCareerStats(playerId).orElseGet(() -> PlayerStatsDTO.empty(playerId));
    }

    public PlayerStatsDTO getPlayerMatchStats(UUID playerId, UUID matchId){
        return perfRepo.getPlayerMatchStats(playerId, matchId).orElseGet(() -> PlayerStatsDTO.empty(playerId));
    }

    public Performance getPlayerSetPerformance(UUID playerId, UUID gameSetId){
        return perfRepo.findByPlayerIdAndGameSetId(playerId, gameSetId).orElseThrow(() -> new ResourceNotFoundException("Il giocatore non ha giocato nel set selezionato"));
    }

    public TeamStatsDTO getTeamMatchStats(UUID teamId, UUID matchId){
        return perfRepo.getTeamMatchStats(teamId , matchId).orElseGet(() -> TeamStatsDTO.empty(teamId));
    }

    public TeamStatsDTO getTeamStats(UUID teamId){
        return perfRepo.getTeamGlobalStats(teamId).orElseGet(() -> TeamStatsDTO.empty(teamId));
    }

    public List<TeamStatsDTO> getBothTeamMatchStats(UUID matchId){
        return perfRepo.getBothTeamsStatsForMatch(matchId);
    }
}

