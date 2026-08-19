package it.unifi.volleyballscouting.controller;
import it.unifi.volleyballscouting.dto.PlayerStatsDTO;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
        prepareFormModel(model, PlayerFormDto.empty());
        return "players/form";
    }
    @PostMapping("/create")
    public String createPlayer(@Valid @ModelAttribute("playerForm") PlayerFormDto form, BindingResult br, Model model, RedirectAttributes redirectAttributes) {

        if(br.hasErrors()) {
            prepareFormModel(model, form);
            return "players/form";
        }
        Team t = coachService.getCoachTeam();
        if (form.number() != null && playerService.isNumberAlreadyTaken(t.getId(), form.number(), null)){
            br.rejectValue("number", "duplicate", "Questo numero è già presente in squadra");
            prepareFormModel(model, form);
            return "players/form";
        }
        String tempPassword = playerService.save(form, t);
        redirectAttributes.addFlashAttribute("tempPassword", tempPassword);
        redirectAttributes.addFlashAttribute("successMessage", "Giocatore creato con successo!");
        return "redirect:/players";
    }
    @GetMapping("/{playerId}/edit")
    public String showEditForm(@PathVariable Long playerId, Model model){
        Player p = playerService.findById(playerId);
        prepareFormModel(model, PlayerFormDto.fromEntity(p));
        return "players/form";
    }
    @PostMapping("/{playerId}/update")
    public String editPlayer(@PathVariable Long playerId,
                             @Valid @ModelAttribute("playerForm")
                             PlayerFormDto form,
                             BindingResult br,
                             Model model){
        if(br.hasErrors()) {
            prepareFormModel(model, form);
            return "players/form";
        }
        Team t = coachService.getCoachTeam();
        if (form.number() != null && playerService.isNumberAlreadyTaken(t.getId(), form.number(), playerId)){
            br.rejectValue("number", "duplicate", "Questo numero è già presente in squadra");
            prepareFormModel(model, form);
            return "players/form";
        }
        playerService.updatePlayer(playerId, form);
        return "redirect:/players/" + playerId;
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
        List<Player> players;

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

    @GetMapping("/details/{playerId}")
    public String showPlayerDetails(@PathVariable Long playerId, Model model){
        Player player = playerService.findById(playerId);
        //TODO: add Performances via the service
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
