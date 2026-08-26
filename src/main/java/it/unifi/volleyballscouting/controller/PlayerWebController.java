package it.unifi.volleyballscouting.controller;
import it.unifi.volleyballscouting.dto.PlayerStatsDTO;
import it.unifi.volleyballscouting.model.Performance;
import it.unifi.volleyballscouting.model.Player;
import it.unifi.volleyballscouting.model.PlayerRole;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.CoachRepository;
import it.unifi.volleyballscouting.repository.PlayerRepository;
import it.unifi.volleyballscouting.security.AppUserDetails;
import it.unifi.volleyballscouting.service.CoachService;
import it.unifi.volleyballscouting.service.PerformanceService;
import it.unifi.volleyballscouting.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import it.unifi.volleyballscouting.dto.PlayerFormDto;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/players")
public class PlayerWebController {

    private final PlayerService playerService;
    private final CoachService coachService;
    private final CoachRepository coachRepository;
    private final PlayerRepository playerRepository;
    private final PerformanceService performanceService;

    public PlayerWebController(PlayerService ps, CoachService cs, CoachRepository cr, PlayerRepository pr, PerformanceService performanceService){
        this.playerService = ps;
        this.coachService = cs;
        this.coachRepository = cr;
        this.playerRepository = pr;
        this.performanceService = performanceService;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        prepareFormModel(model, PlayerFormDto.empty(), "/players/create");
        return "players/form";
    }
    @PostMapping("/create")
    public String createPlayer(@AuthenticationPrincipal AppUserDetails userDetails,
                            @Valid @ModelAttribute("playerForm") PlayerFormDto form, BindingResult br, Model model,
                            RedirectAttributes redirectAttributes) {

        if(br.hasErrors()) {
            prepareFormModel(model, form, "/players/create");
            return "players/form";
        }
        if(coachRepository.findByUsername(form.username()).isPresent() ||
        playerRepository.findByUsername(form.username()).isPresent()){
            br.rejectValue("username", "duplicate", "L'username è già in uso");
            prepareFormModel(model, form);
            return "players/form";
        }
        Team t = coachService.getCoachTeam(userDetails.getId());
        if (form.number() != null && playerService.isNumberAlreadyTaken(t.getId(), form.number(), null)){
            br.rejectValue("number", "duplicate", "Questo numero è già presente in squadra");
            prepareFormModel(model, form, "/players/create");
            return "players/form";
        }
        String tempPassword = playerService.save(form, t);
        redirectAttributes.addFlashAttribute("tempPassword", tempPassword);
        redirectAttributes.addFlashAttribute("successMessage", "Giocatore creato con successo!");
        // FIX: prima era "redirect:/players" (nessun handler su /players) -> ora /players/list
        return "redirect:/players/list";
    }
    @GetMapping("/{playerId}/edit")
    public String showEditForm(@PathVariable UUID playerId, Model model){
        Player p = playerService.findById(playerId);
        prepareFormModel(model, PlayerFormDto.fromEntity(p), "/players/" + playerId + "/update");
        return "players/form";
    }
    @PostMapping("/{playerId}/update")
    public String editPlayer(@AuthenticationPrincipal AppUserDetails userDetails,
                            @PathVariable UUID playerId,
                            @Valid @ModelAttribute("playerForm")
                            PlayerFormDto form,
                            BindingResult br,
                            Model model){
        if(br.hasErrors()) {
            prepareFormModel(model, form, "/players/" + playerId + "/update");
            return "players/form";
        }
        Team t = coachService.getCoachTeam(userDetails.getId());
        if (form.number() != null && playerService.isNumberAlreadyTaken(t.getId(), form.number(), playerId)){
            br.rejectValue("number", "duplicate", "Questo numero è già presente in squadra");
            prepareFormModel(model, form, "/players/" + playerId + "/update");
            return "players/form";
        }
        playerService.updatePlayer(playerId, form);
        // FIX: prima era "redirect:/players/" + playerId (nessun handler) -> ora /players/details/{id}
        return "redirect:/players/details/" + playerId;
    }
    // GET: show list of players
    @GetMapping("/list")
    public String listPlayers(
            @RequestParam(required = false) UUID teamId,
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
        model.addAttribute("roles", PlayerRole.values());
        return "players/list";
    }

    @GetMapping("/details/{playerId}")
    public String showPlayerDetails(@PathVariable UUID playerId, Model model){
        Player player = playerService.findById(playerId);
        PlayerStatsDTO stats = performanceService.getPlayerCareerStats(player);
        model.addAttribute("player", player);
        model.addAttribute("stats", stats);
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
        model.addAttribute("roles", PlayerRole.values());
        return "players/NoTeamList";
    }

    private void prepareFormModel(Model model, PlayerFormDto form, String formAction){
        model.addAttribute("playerForm", form);
        model.addAttribute("roles", PlayerRole.values());
        model.addAttribute("formAction", formAction);
    }

}
