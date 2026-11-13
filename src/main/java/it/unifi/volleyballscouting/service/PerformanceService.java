package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.stats.PlayerStatsDTO;
import it.unifi.volleyballscouting.dto.stats.TeamStatsDTO;
import it.unifi.volleyballscouting.model.*;
import it.unifi.volleyballscouting.query.PerformanceQuery;
import it.unifi.volleyballscouting.repository.PerformanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PerformanceService {

    private final PerformanceRepository perfRepo;
	private final PlayerService playerService;

	public PerformanceService(PerformanceRepository repo, PlayerService playerService){
        this.perfRepo = repo;
		this.playerService = playerService;
	}

    public List<Performance> findFiltered(PerformanceQuery q){
        return perfRepo.findAll(q.toSpecification());

    }

    //Aggregated Queries
	//TODO: change all the object to the uuids

    public PlayerStatsDTO getPlayerCareerStats(UUID playerId){
        return perfRepo.getPlayerCareerStats(playerId).orElseGet(() -> PlayerStatsDTO.empty(playerId));
    }

    public PlayerStatsDTO getPlayerMatchStats(UUID playerId, UUID matchId){
        return perfRepo.getPlayerMatchStats(playerId, matchId).orElseGet(() -> PlayerStatsDTO.empty(playerId));
    }

    public Performance getPlayerSetPerformance(UUID playerId, UUID gameSetId){
        return perfRepo.findByPlayerIdAndGameSetId(playerId, gameSetId).orElseThrow(() -> new EntityNotFoundException("Il giocatore non ha giocato nel set selezionato"));
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

