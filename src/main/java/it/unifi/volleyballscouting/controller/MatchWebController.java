package it.unifi.volleyballscouting.controller;

import it.unifi.volleyballscouting.dto.MatchFormDTO;
import it.unifi.volleyballscouting.security.AppUserDetails;
import it.unifi.volleyballscouting.service.MatchService;
import it.unifi.volleyballscouting.service.RefereeService;
import it.unifi.volleyballscouting.service.TeamService;
import it.unifi.volleyballscouting.validation.OnCreate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String createMatch(@AuthenticationPrincipal AppUserDetails userDetails,
                              @Validated(OnCreate.class) @ModelAttribute("matchForm") MatchFormDTO form, BindingResult br, Model model,
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


    private void prepareFormModel(Model model, MatchFormDTO form, String formAction) {
        model.addAttribute("matchForm", form);
        model.addAttribute("formAction", formAction);
        model.addAttribute("teams", teamService.findAll());
        model.addAttribute("referee", refereeService.findAll());
    }
}

