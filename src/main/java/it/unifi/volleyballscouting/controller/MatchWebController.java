package it.unifi.volleyballscouting.controller;

import it.unifi.volleyballscouting.dto.MatchFormDTO;
import it.unifi.volleyballscouting.exception.BusinessRuleException;
import it.unifi.volleyballscouting.model.Match;
import it.unifi.volleyballscouting.query.MatchQuery;
import it.unifi.volleyballscouting.service.MatchService;
import it.unifi.volleyballscouting.service.RefereeService;
import it.unifi.volleyballscouting.service.TeamService;
import it.unifi.volleyballscouting.validation.OnCreate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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
            return "matches/form";
        }
        try{
            matchService.save(form);
        }catch (IllegalArgumentException e){
            br.reject("matchRules" , e.getMessage());
            prepareFormModel(model, form, "/matches/create");
            return "matches/form";
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
    @PostMapping("/{matchId}/recreate")
    public String recreateMatch(@PathVariable UUID matchId,
                              @Validated(OnCreate.class) @ModelAttribute("matchForm") MatchFormDTO form,
                              BindingResult br, Model model,
                              RedirectAttributes redirectAttributes){
        if(br.hasErrors()){
            prepareFormModel(model, form, "/matches/" + matchId + "/recreate");
            return "matches/form";
        }
        try{
            Match created = matchService.recreate(matchId, form);
            redirectAttributes.addFlashAttribute("successMessage", "La partita è stata ricreata");
            return "redirect:/matches/details/" + created.getId();
        }catch(BusinessRuleException e){
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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate to,
            @RequestParam(required = false) String city,
            Model model) {
        MatchQuery query = new MatchQuery.Builder()
                .team1Id(team1).team2Id(team2).from(from).to(to).city(city).build();
        model.addAttribute("matches", matchService.search(query));
        model.addAttribute("teams", teamService.findAll());
        return "matches/list";
    }
    @GetMapping("/{matchId}/details")
    public String matchDetails(@PathVariable UUID matchId, Model model){
        Match match = matchService.findById(matchId);
        model.addAttribute("match", match);
        return "matches/details";
    }
    @PostMapping("/{matchId}/delete")
    public String deleteMatch(@PathVariable UUID matchId, RedirectAttributes ra){
        try{
            matchService.delete(matchId);
            ra.addFlashAttribute("successMessage", "Partita cancellata");
        }catch(IllegalArgumentException e){
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/matches/list";
    }


    private void prepareFormModel(Model model, MatchFormDTO form, String formAction) {
        model.addAttribute("matchForm", form);
        model.addAttribute("formAction", formAction);
        model.addAttribute("teams", teamService.findAll());
        model.addAttribute("referee", refereeService.findAll());
        //Important: String formAction is needed for telling to which url data must be sent. Or we have to create two form pages
    }
}

