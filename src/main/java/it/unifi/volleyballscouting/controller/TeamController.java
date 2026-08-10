package it.unifi.volleyballscouting.controller;

import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // Dice a Spring che questa classe gestisce richieste web
@RequestMapping("/api/teams") // Tutti gli indirizzi inizieranno con /api/teams
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    // 1. GET: Leggere tutte le squadre
    // Provalo su: http://localhost:8080/api/teams
    @GetMapping
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    // Adesso puoi scrivere: http://localhost:8080/api/teams/add/SavinoDelBene
    @GetMapping("/add/{nome}")
    public Team createTeamQuick(@PathVariable String nome) {
        Team nuovoTeam = new Team();
        nuovoTeam.setName(nome);
        return teamRepository.save(nuovoTeam); // Salva nel DB e ti mostra il risultato
    }
}
