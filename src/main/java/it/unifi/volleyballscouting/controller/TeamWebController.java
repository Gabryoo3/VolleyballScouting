package it.unifi.volleyballscouting.controller;

import it.unifi.volleyballscouting.dto.TeamFormDto;
import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.service.TeamService;
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

    public TeamWebController(TeamService ts){
        this.teamService = ts;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("teamForm", TeamFormDto.empty());
        return "teams/form";
    }
    @PostMapping("/create")
    public String CreateTeam(
            @Valid @ModelAttribute("teamForm")
            TeamFormDto form,
            BindingResult br,
            @AuthenticationPrincipal Coach coach,
            Model model
    ){
        if (br.hasErrors()) {
            prepareFormModel(model, form);
            return "teams/form";
        }
        teamService.save(form, coach);
        return "redirect:/teams";
    }
    @GetMapping("/{teamId}/edit")
    public String showEditForm(@PathVariable UUID teamId, Model model){
        Team t = teamService.findById(teamId);
        prepareFormModel(model,TeamFormDto.fromEntity(t));
        return "teams/form";
    }
    @PostMapping("/{teamId}/update")
    public String editTeam(
            @PathVariable UUID teamId,
            @Valid @ModelAttribute("teamForm")
            TeamFormDto form,
            BindingResult br,
            Model model){
        if(br.hasErrors()){
            prepareFormModel(model, form);
            return "teams/form";
        }
        teamService.updateTeam(teamId, form);
        return "redirect:/teams" + teamId;
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

    private void prepareFormModel(Model model, TeamFormDto form){
        model.addAttribute("teamForm", form);
    }

}
