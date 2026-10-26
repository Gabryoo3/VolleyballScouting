package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.MatchFormDTO;
import it.unifi.volleyballscouting.model.Match;
import it.unifi.volleyballscouting.model.Referee;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.MatchRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Transactional(readOnly = true)
@Service
public class MatchService {

    private static final Duration MATCH_DURATION = Duration.ofHours(3);
    private static final Duration TEAM_REST = Duration.ofHours(4);

    private final MatchRepository matchRepository;
    private final TeamService teamService;
    private final RefereeService refereeService;

    public MatchService(MatchRepository repo, TeamService ts, RefereeService refereeService){
        this.matchRepository = repo;
        this.teamService = ts;
        this.refereeService = refereeService;
    }

    public Match findById(UUID id){
        return matchRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Partita non trovata"));
    }

    public List<Match> findAll(){
        return matchRepository.findAll(Sort.by("matchDateTime").descending());
    }


    boolean existsRefereeDoubleMatch(UUID refereeId, LocalDateTime start, LocalDateTime end){
        return matchRepository.existsByRefereeIdAndMatchDateTimeAfterAndMatchDateTimeBefore(refereeId, start, end);
    }
    boolean existsTeamDoubleMatch(UUID teamId, LocalDateTime start, LocalDateTime end){
        return matchRepository.existsByTeamIdAndMatchDateTimeAfterAndMatchDateTimeBefore(teamId, start, end);
    }


    //TODO: check if it works

    @Transactional
    public Match save(MatchFormDTO form){
        if (form.teamHomeId().equals(form.teamGuestId()))
            throw new IllegalArgumentException("Non si può impostare una squadra contro se stessa");
        LocalDateTime windowRefereeStart = form.matchDateTime().minus(MATCH_DURATION);
        LocalDateTime windowRefereeEnd = form.matchDateTime().plus(MATCH_DURATION);
        if(existsRefereeDoubleMatch(form.refereeId(), windowRefereeStart, windowRefereeEnd))
            throw new IllegalArgumentException("L'arbitro ha già una partita nella fascia oraria");
        LocalDateTime windowTeamStart = form.matchDateTime().minus(TEAM_REST);
        LocalDateTime windowTeamEnd = form.matchDateTime().plus(TEAM_REST);
        if(existsTeamDoubleMatch(form.teamHomeId(), windowTeamStart, windowTeamEnd))
            throw new IllegalArgumentException("La squadra ha già una partita nella fascia oraria");
        if(existsTeamDoubleMatch(form.teamGuestId(), windowTeamStart, windowTeamEnd))
            throw new IllegalArgumentException("La squadra ha già una partita nella fascia oraria");

        Team home = teamService.findById(form.teamHomeId());
        Team guest = teamService.findById(form.teamGuestId());
        Referee r = refereeService.findById(form.refereeId());

        Match match = new Match(home, guest, form.matchDateTime(), r);
        return matchRepository.save(match);
    }
    @Transactional
    public void delete(UUID matchId){
        Match m = findById(matchId);
        if (!m.getSets().isEmpty()){
            throw new IllegalArgumentException("La partita è già iniziata");
        }
        matchRepository.delete(m);
    }
    @Transactional
    public Match recreate(UUID oldMatch, MatchFormDTO form){
        delete(oldMatch);
        matchRepository.flush();
        return save(form);
    }



}
