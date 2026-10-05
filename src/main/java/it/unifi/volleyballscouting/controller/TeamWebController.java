package it.unifi.volleyballscouting.controller;

import it.unifi.volleyballscouting.dto.TeamFormDTO;
import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.security.AppUserDetails;
import it.unifi.volleyballscouting.service.TeamService;
import it.unifi.volleyballscouting.service.CoachService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/teams")
public class TeamWebController {

    private final TeamService teamService;
    private final CoachService coachService;

    public TeamWebController(TeamService ts, CoachService cs){
        this.teamService = ts;
        this.coachService = cs;
    }

    @GetMapping("/new")
    public String showCreateForm(@AuthenticationPrincipal AppUserDetails userDetails, Model model){
        // 1. Se l'utente non è un Coach (es. è un Admin puro), non può creare una squadra per se stesso
        if (!userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COACH"))) {
            return "redirect:/teams";
        }

        // 2. Se il Coach ha già una squadra associata, lo rimandiamo al dettaglio della sua squadra
        Team existingTeam = coachService.getCoachTeam(userDetails.getId());
        if (existingTeam != null) {
            return "redirect:/teams/details/" + existingTeam.getId();
        }

        prepareFormModel(model, TeamFormDTO.empty(), "/teams/create");
        return "teams/form";
    }

    @PostMapping("/create")
    public String CreateTeam(
            @Valid @ModelAttribute("teamForm") TeamFormDTO form,
            BindingResult br,
            @AuthenticationPrincipal AppUserDetails userDetails,
            Model model
    ){
        // Impedisce all'Admin di chiamare l'endpoint legando a sé la squadra
        if (!userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COACH"))) {
            br.reject("notCoach", "Solo un allenatore accreditato può registrare una squadra.");
            prepareFormModel(model, form, "/teams/create");
            return "teams/form";
        }

        // Verifica che il Coach non abbia già una squadra
        if (coachService.getCoachTeam(userDetails.getId()) != null) {
            br.reject("alreadyHasTeam", "Hai già una squadra associata.");
            prepareFormModel(model, form, "/teams/create");
            return "teams/form";
        }

        if (br.hasErrors()) {
            prepareFormModel(model, form, "/teams/create");
            return "teams/form";
        }

        teamService.save(form, userDetails.getId());
        return "redirect:/teams";
    }
    @GetMapping("/{teamId}/edit")
    public String showEditForm(@PathVariable UUID teamId, Model model){
        Team t = teamService.findById(teamId);
        prepareFormModel(model, TeamFormDTO.fromEntity(t), "/teams/" + teamId + "/update");
        return "teams/form";
    }
    @PostMapping("/{teamId}/update")
    public String editTeam(
            @PathVariable UUID teamId,
            @Valid @ModelAttribute("teamForm")
            TeamFormDTO form,
            BindingResult br,
            Model model){
        if(br.hasErrors()){
            prepareFormModel(model, form, "/teams/" + teamId + "/update");
            return "teams/form";
        }
        teamService.updateTeam(teamId, form);
        // FIX: prima era "redirect:/teams" + teamId (mancava lo "/") -> ora /teams/details/{id}
        return "redirect:/teams/details/" + teamId;
    }

    @GetMapping String listTeams(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            Model model){
        String cleanName = (name != null && !name.isBlank()) ? name : null;
        String cleanCity = (city != null && !city.isBlank()) ? city : null;
        List<Team> teams;

        if (cleanName != null)
            if (cleanCity != null)
                teams = teamService.findByNameAndCity(cleanName, cleanCity);
            else
                teams = teamService.findByName(cleanName);
        else if (cleanCity != null)
            teams = teamService.findByCity(cleanCity);
        else
            teams = teamService.findAll();
        model.addAttribute("teams", teams);
        return "teams/list";
    }

    @GetMapping("/details/{teamId}")
    public String showTeamDetails(@PathVariable UUID teamId, Model model){
        Team team = teamService.findById(teamId);
        //TODO: add Performances via the service
        model.addAttribute("team", team);
        return "teams/detail";
    }

    private void prepareFormModel(Model model, TeamFormDTO form, String formAction){
        model.addAttribute("teamForm", form);
        model.addAttribute("formAction", formAction);
    }

}
