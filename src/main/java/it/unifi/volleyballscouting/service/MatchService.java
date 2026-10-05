package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.model.Match;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.MatchRepository;
import it.unifi.volleyballscouting.repository.TeamRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    public MatchService(MatchRepository repo, TeamRepository teamRepository){
        this.matchRepository = repo;
        this.teamRepository = teamRepository;
    }

    public Match findById(UUID id){
        return matchRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Partita non trovata"));
    }

    public List<Match> findAll(){
        return matchRepository.findAll(Sort.by("matchDateTime").descending());
    }

    public List<Match> findByMatchDateTimeAfterNow(LocalDateTime now){
        return matchRepository.findByMatchDateTimeAfterOrderByMatchDateTimeAsc(now);
    }

    public List<Match> findByMatchDateTimeBeforeNow(LocalDateTime now){
        return matchRepository.findByMatchDateTimeBeforeOrderByMatchDateTimeDesc(now);
    }

    public List<Match> findByCity(String city){
        return matchRepository.findByAddressCityContaining(city);
    }

    public List <Match> findByMatchDateTimeBetween(LocalDateTime start, LocalDateTime end){
        return matchRepository.findByMatchDateTimeBetween(start, end);
    }
    public List<Match> findByTeamId(UUID teamId){
        return matchRepository.findByTeam(teamId);
    }
    public List<Match> findByTeamWins(UUID teamId){
        return matchRepository.findByTeamWins(teamId);
    }
    public List<Match> findByTeamLosses(UUID teamId){
        return matchRepository.findByTeamLosses(teamId);
    }
    public List<Match> findByTeam1vsTeam2(UUID team1Id, UUID team2Id){
        return matchRepository.findByTeam1vsTeam2(team1Id, team2Id);
    }

    @Transactional
    public void save(MatchFormDTO form)



}
