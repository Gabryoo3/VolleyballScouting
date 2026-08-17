package it.unifi.volleyballscouting.controller;
import it.unifi.volleyballscouting.model.Player;
import it.unifi.volleyballscouting.model.PlayerRole;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.service.CoachService;
import it.unifi.volleyballscouting.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import it.unifi.volleyballscouting.dto.PlayerFormDto;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/players")
public class PlayerWebController {

    private final PlayerService playerService;
    private final CoachService coachService;

    public PlayerWebController(PlayerService ps, CoachService cs){
        this.playerService = ps;
        this.coachService = cs;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        prepareFormModel(model, new PlayerFormDto(null, null, null, null, null, null, null, null));
        return "players/form";
    }
    @PostMapping("/create")
    public String createPlayer(@Valid @ModelAttribute("playerForm") PlayerFormDto form, BindingResult br, Model model) {

        if(br.hasErrors()) {
            prepareFormModel(model, form);
            return "players/form";
        }
        Team t = coachService.getCoachTeam();
        if (playerService.isNumberAlreadyTaken(t.getId(), form.number())){
            br.rejectValue("number", "duplicate", "Questo numero è già presente in squadra");
            prepareFormModel(model, form);
            return "players/form";
        }
        playerService.save(form, t);
        return "redirect:/players";
    }
    // GET: show list of players
    @GetMapping("/list")
    public String listPlayers(
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Integer number,
            Model model) {

        String cleanSurname = (surname != null && !surname.isBlank()) ? surname : null;
        String cleanRole = (role != null && !role.isBlank()) ? role : null;
        List<Player> players = new ArrayList<>();

        if (teamId != null) {
            if (number != null) {
                Player player = playerService.findByTeamIdAndNumber(teamId, number);
                players = (player != null) ? List.of(player) : List.of();
            } else if (cleanSurname != null) {
                players = playerService.findByTeamIdAndSurname(teamId, cleanSurname);
            } else if (cleanRole != null) {
                players = playerService.findByTeamIdAndRole(teamId, cleanRole);
            } else {
                players = playerService.findByTeamId(teamId);
            }
        }
        else if (cleanSurname != null) {
            players = playerService.findBySurname(cleanSurname);
        } else if (cleanRole != null) {
            players = playerService.findByRole(cleanRole);
        } else if (number != null) {
            players = playerService.findByNumber(number);
        }
        else {
            players = playerService.findAll();
        }

        model.addAttribute("players", players);
        return "players/list";
    }

    @GetMapping("/{playerId}")
    public String showPlayerDetails(@PathVariable Long playerId, Model model){
        Player player = playerService.findById(playerId);
        model.addAttribute("player", player);
        return "players/detail";
    }
    @GetMapping("/free")
    public String showFreePlayers(@RequestParam(required = false) String role, Model model){
        String cleanRole = (role != null && !role.isBlank()) ? role : null;
        List<Player> players;
        if(cleanRole != null){
            players = playerService.findByTeamIdIsNullAndRole(cleanRole);
        }else{
            players = playerService.findByTeamIdIsNull();
        }
        model.addAttribute("players", players);
        return "players/NoTeamList";
    }

    private void prepareFormModel(Model model, PlayerFormDto form){
        model.addAttribute("playerForm", form);
        model.addAttribute("roles", PlayerRole.values());
    }

}
