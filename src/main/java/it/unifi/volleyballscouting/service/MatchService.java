package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.MatchFormDTO;
import it.unifi.volleyballscouting.exception.BusinessRuleException;
import it.unifi.volleyballscouting.exception.ResourceNotFoundException;
import it.unifi.volleyballscouting.model.GameSet;
import it.unifi.volleyballscouting.model.Match;
import it.unifi.volleyballscouting.model.Referee;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.query.MatchQuery;
import it.unifi.volleyballscouting.repository.MatchRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
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
    private static final Duration START_MATCH = Duration.ofHours(1);

    private final MatchRepository matchRepository;
    private final TeamService teamService;
    private final RefereeService refereeService;

    public MatchService(MatchRepository repo, TeamService ts, RefereeService refereeService){
        this.matchRepository = repo;
        this.teamService = ts;
        this.refereeService = refereeService;
    }

    public Match findById(UUID id){
        return matchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Partita non trovata"));
    }

    public List<Match> findAll(){
        return matchRepository.findAll(Sort.by("matchDateTime").descending());
    }

    public List<Match> search(MatchQuery query) {
        return matchRepository.findAll(query.toSpecification(), Sort.by("matchDateTime").descending());
    }

    public List<Match> findAssignedTo(UUID refereeId){
        return matchRepository.findByRefereeIdOrderByMatchDateTimeAsc(refereeId);
    }

    boolean existsRefereeDoubleMatch(UUID refereeId, LocalDateTime start, LocalDateTime end){
        return matchRepository.existsByRefereeIdAndMatchDateTimeAfterAndMatchDateTimeBefore(refereeId, start, end);
    }
    boolean existsTeamDoubleMatch(UUID teamId, LocalDateTime start, LocalDateTime end){
        return matchRepository.existsByTeamIdAndMatchDateTimeAfterAndMatchDateTimeBefore(teamId, start, end);
    }

    public Match findRefereedBy (UUID matchId, UUID refereeId){
        Match m = findById(matchId);
        if (!m.getReferee().getId().equals(refereeId))
            throw new AccessDeniedException("Non sei l'arbitro di questa partita");
        return m;
    }

    @Transactional
    public void start(UUID matchId, UUID refereeId){
        Match m = findRefereedBy(matchId, refereeId);
        if (LocalDateTime.now().isBefore(m.getMatchDateTime().plus(START_MATCH)))
            throw new BusinessRuleException("È ancora troppo presto per avviare la partita");
        m.start();
    }

    @Transactional
    public void closeSet(UUID matchId, UUID refereeId, int setNumber, int home, int guest){
        Match m = findRefereedBy(matchId, refereeId);
        GameSet current = m.getCurrentSet();
        if (current == null || current.getSetNumber() != setNumber)
            throw new BusinessRuleException("Il set indicato non è quello in corso");
        m.closeCurrentSet(home,guest);
    }

    @Transactional
    public Match save(MatchFormDTO form){
        if (form.teamHomeId().equals(form.teamGuestId()))
            throw new BusinessRuleException("Non si può impostare una squadra contro se stessa");
        LocalDateTime windowRefereeStart = form.matchDateTime().minus(MATCH_DURATION);
        LocalDateTime windowRefereeEnd = form.matchDateTime().plus(MATCH_DURATION);
        if(existsRefereeDoubleMatch(form.refereeId(), windowRefereeStart, windowRefereeEnd))
            throw new BusinessRuleException("L'arbitro ha già una partita nella fascia oraria");
        LocalDateTime windowTeamStart = form.matchDateTime().minus(TEAM_REST);
        LocalDateTime windowTeamEnd = form.matchDateTime().plus(TEAM_REST);
        if(existsTeamDoubleMatch(form.teamHomeId(), windowTeamStart, windowTeamEnd))
            throw new BusinessRuleException("La squadra ha già una partita nella fascia oraria");
        if(existsTeamDoubleMatch(form.teamGuestId(), windowTeamStart, windowTeamEnd))
            throw new BusinessRuleException("La squadra ha già una partita nella fascia oraria");

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
            throw new BusinessRuleException("La partita è già iniziata");
        }else if (m.getMatchDateTime().isBefore(LocalDateTime.now())){
            throw new BusinessRuleException("Non si può cancellare un match concluso o già avviato");
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
