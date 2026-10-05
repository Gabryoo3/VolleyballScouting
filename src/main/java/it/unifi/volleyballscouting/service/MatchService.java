package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.model.Match;
import it.unifi.volleyballscouting.repository.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional (readOnly = true)
@Service
public class MatchService {
    private final MatchRepository matchRepository;

    public MatchService(MatchRepository mr){
        this.matchRepository = mr;
    }

    public List<Match> findAll() { return matchRepository.findAll();}

    public Match findById(UUID id){
        return matchRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Match non trovato"));
    }
}
