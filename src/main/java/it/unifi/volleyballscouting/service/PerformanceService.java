package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.scouting.ScoutRowDTO;
import it.unifi.volleyballscouting.dto.scouting.ScoutSetFormDTO;
import it.unifi.volleyballscouting.dto.stats.PlayerStatsDTO;
import it.unifi.volleyballscouting.dto.stats.TeamStatsDTO;
import it.unifi.volleyballscouting.exception.BusinessRuleException;
import it.unifi.volleyballscouting.exception.ResourceNotFoundException;
import it.unifi.volleyballscouting.model.*;
import it.unifi.volleyballscouting.query.PerformanceQuery;
import it.unifi.volleyballscouting.repository.PerformanceRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
@Transactional(readOnly = true)
public class PerformanceService {

    private final PerformanceRepository perfRepo;
	private final MatchService matchService;
	private final CoachService coachService;
	private final PlayerService playerService;

	public PerformanceService(PerformanceRepository repo, MatchService matchService, CoachService coachService, PlayerService playerService){
        this.perfRepo = repo;
		this.matchService = matchService;
		this.coachService = coachService;
		this.playerService = playerService;
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

	@Transactional
	public void saveSetScouting(UUID matchId, UUID coachId, int setNumber, ScoutSetFormDTO form){
		Match m = matchService.findById(matchId);
		Team t = coachService.getCoachTeam(coachId);
		if (t == null) throw new BusinessRuleException("Non hai una squadra ancora");
		if (!t.getId().equals(m.getTeamHome().getId()) || !t.getId().equals(m.getTeamGuest().getId()))
			throw new AccessDeniedException("La tua squadra non gioca questa partita");

		//.stream() allows to work on the list, then it filters for the first SetNumber it finds
		// (since they are given from the match, no problems in taking other sets) and it takes the first set it matches
		GameSet set = m.getSets().stream().filter(s -> s.getSetNumber() == setNumber).findFirst()
				.orElseThrow(() -> new BusinessRuleException("Il set indicato non esiste "));
		//in case of a re-send, it deletes the entry and recreates it
		if (perfRepo.existsByTeamIdAndGameSetId(t.getId(), set.getId()))
			throw new BusinessRuleException("Il risultato è già stato inviato");

		//HashSet is used because it already verifies if in the same send there is a duplication
		Set<UUID> seen = new HashSet<>();
		for (ScoutRowDTO row : form.rows()){
			if(row.isEmpty()) continue;
			if(!seen.add(row.playerId())) throw new BusinessRuleException("Giocatore ripetuto nel set");
			Player p = playerService.findOwnedBy(row.playerId(), t.getId()); //checks if the player is in the team
			Performance perf = new Performance(p, set, t);
			perf.setCounters(row);
			perfRepo.save(perf);
		}
	}
}

