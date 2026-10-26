package it.unifi.volleyballscouting.controller;

import it.unifi.volleyballscouting.dto.MatchFormDTO;
import it.unifi.volleyballscouting.model.Match;
import it.unifi.volleyballscouting.service.MatchService;
import it.unifi.volleyballscouting.service.RefereeService;
import it.unifi.volleyballscouting.service.TeamService;
import it.unifi.volleyballscouting.validation.OnCreate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/matches")
public class MatchWebController {

    private final MatchService matchService;
    private final TeamService teamService;
    private final RefereeService refereeService;


    public MatchWebController(MatchService ms, TeamService teamService, RefereeService refereeService) {
        this.matchService = ms;
        this.teamService = teamService;
        this.refereeService = refereeService;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        prepareFormModel(model, MatchFormDTO.empty(), "/matches/create");
        return "matches/form";
    }
    @PostMapping("/create")
    public String createMatch(  @Validated(OnCreate.class)
                                @ModelAttribute("matchForm") MatchFormDTO form, BindingResult br, Model model,
                                RedirectAttributes redirectAttributes){
        if(br.hasErrors()) {
            prepareFormModel(model, form, "/matches/create");
            return "/matches/form";
        }
        try{
            matchService.save(form);
        }catch (IllegalArgumentException e){
            br.reject("matchRules" , e.getMessage());
            prepareFormModel(model, form, "/matches/create");
            return "/matches/form";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Match creato con successo!");
        return "redirect:/matches/list";
    }
    @GetMapping("/{matchId}/edit")
    public String showEditForm (@PathVariable UUID matchId,
                                Model model){
        Match m = matchService.findById(matchId);
        prepareFormModel(model, MatchFormDTO.fromEntity(m), "/matches/" + matchId + "/recreate");
        return "matches/form";
    }
    @PostMapping("/{matchId/recreate")
    public String updateMatch(@PathVariable UUID matchId,
                              @Validated(OnCreate.class) @ModelAttribute("matchForm") MatchFormDTO form,
                              BindingResult br, Model model,
                              RedirectAttributes redirectAttributes){
        if(br.hasErrors()){
            prepareFormModel(model, form, "/matches/" + matchId + "/update");
            return "/players/form";
        }
        try{
            Match created = matchService.recreate(matchId, form);
            redirectAttributes.addFlashAttribute("successMessage", "La partita è stata ricreata");
            return "redirect:/matches/details" + created.getId();
        }catch(IllegalArgumentException e){
            br.reject("matchRules", e.getMessage());
            prepareFormModel(model, form, "/matches/" + matchId + "/recreate");
            return "matches/form";
        }
        //TODO: make routes and list of matches
    }
    @GetMapping("/list")
    public String listMatches(
        @RequestParam(required = false) UUID team1,
        @RequestParam(required = false) UUID team2,
        @RequestParam(required = false) String when, //used for filtering matches before or after certain date
        @RequestParam(required = false) LocalDateTime matchDate,
        Model model){
        List<Match> matchList;
        if(matchDate != null){
            if("upcoming".equals(when)){
                if (team1 != null && team2 != null){
                    matchList = matchService.findByTeam1vsTeam2AfterDate(team1, team2, matchDate);
                }
                else if (team1 != null){
                }
            }
        }

    }


    private void prepareFormModel(Model model, MatchFormDTO form, String formAction) {
        model.addAttribute("matchForm", form);
        model.addAttribute("formAction", formAction);
        model.addAttribute("teams", teamService.findAll());
        model.addAttribute("referee", refereeService.findAll());
        //Important: String formAction is needed for telling to which url data must be sent. Or we should have created two form pages
    }
}

