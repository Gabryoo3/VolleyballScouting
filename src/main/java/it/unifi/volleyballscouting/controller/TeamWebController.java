package it.unifi.volleyballscouting.controller;

import it.unifi.volleyballscouting.dto.forms.TeamFormDTO;
import it.unifi.volleyballscouting.exception.BusinessRuleException;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.security.AppUserDetails;
import it.unifi.volleyballscouting.service.CoachService;
import it.unifi.volleyballscouting.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/teams")
public class TeamWebController {

    private final TeamService teamService;
    private final CoachService coachService; // Aggiunto CoachService

    public TeamWebController(TeamService ts, CoachService cs){
        this.teamService = ts;
        this.coachService = cs;
    }

    @GetMapping("/new")
    public String showCreateForm(@AuthenticationPrincipal AppUserDetails userDetails, Model model){
        // Verifica se il Coach ha già una squadra associata
        Team existingTeam = coachService.getCoachTeam(userDetails.getId());
        if (existingTeam != null) {
            // Se ce l'ha già, lo rimanda alla pagina della sua squadra
            return "redirect:/teams/"+ existingTeam.getId()+ "/details" ;
        }

        prepareFormModel(model, TeamFormDTO.empty(), "/teams/create");
        return "teams/form";
    }

    @PostMapping("/create")
    public String createTeam(
            @Valid @ModelAttribute("teamForm")
            TeamFormDTO form,
            BindingResult br,
            @AuthenticationPrincipal AppUserDetails userDetails,
            RedirectAttributes ra,
            Model model
    ){
        // Verifica di sicurezza aggiuntiva per evitare duplicati
        if (coachService.getCoachTeam(userDetails.getId()) != null) {
            br.reject("alreadyHasTeam", "Hai già una squadra associata. Non puoi crearne un'altra.");
        }

        if (br.hasErrors()) {
            prepareFormModel(model, form, "/teams/create");
            return "teams/form";
        }
        try {
            teamService.save(form, userDetails.getId());
        }
        catch(BusinessRuleException e){
            br.reject("teamRules", e.getMessage());
            prepareFormModel(model, form, "/teams/create");
            return "teams/form";
            }
        ra.addFlashAttribute("successMessage", "Team creato con successo!");
        return "redirect:/teams";
        }

    @GetMapping("/{teamId}/edit")
    public String showEditForm(@PathVariable UUID teamId, @AuthenticationPrincipal AppUserDetails userDetails, Model model){
        Team t = teamService.findOwnedBy(teamId, userDetails.getId());
        prepareFormModel(model, TeamFormDTO.fromEntity(t), "/teams/" + teamId + "/update");
        return "teams/form";
    }

    @PostMapping("/{teamId}/update")
    public String editTeam(
            @PathVariable UUID teamId,
            @Valid @ModelAttribute("teamForm")
            TeamFormDTO form,
            BindingResult br,
            @AuthenticationPrincipal AppUserDetails userDetails,
            RedirectAttributes ra,
            Model model){
        if(br.hasErrors()){
            prepareFormModel(model, form, "/teams/" + teamId + "/update");
            return "teams/form";
        }
        try {
            teamService.updateTeam(teamId, userDetails.getId(), form);
        }catch (BusinessRuleException e){
            br.reject("teamRules", e.getMessage());
            prepareFormModel(model, form, "/teams/" + teamId + "/update");
            return "teams/form";
        }
        ra.addFlashAttribute("successMessage", "Team modificato correttamente!");
        return "redirect:/teams/" + teamId +"/details";
    }

    @GetMapping
    public String listTeams(
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

    @GetMapping("/{teamId}/details")
    public String showTeamDetails(@AuthenticationPrincipal AppUserDetails userDetails, @PathVariable UUID teamId, Model model){
		UUID viewerId = userDetails != null ? userDetails.getId() : null;
        model.addAttribute("team", teamService.getDetails(teamId, viewerId));
        return "teams/detail";
    }

    private void prepareFormModel(Model model, TeamFormDTO form, String formAction){
        model.addAttribute("teamForm", form);
        model.addAttribute("formAction", formAction);
    }
}