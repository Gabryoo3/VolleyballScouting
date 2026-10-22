package it.unifi.volleyballscouting.controller;

import it.unifi.volleyballscouting.dto.MatchFormDTO;
import it.unifi.volleyballscouting.security.AppUserDetails;
import it.unifi.volleyballscouting.service.MatchService;
import it.unifi.volleyballscouting.service.UserAccountService;
import it.unifi.volleyballscouting.validation.OnCreate;
import jakarta.validation.Valid;
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
    private final UserAccountService userAccountService;

    public MatchWebController(MatchService ms, UserAccountService uas) {
        this.matchService = ms;
        this.userAccountService = uas;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        prepareFormModel(model, MatchFormDTO.empty(), "/matches/create");
        return "players/form";
    }
    @PostMapping("/create")
    public String createMatch(@AuthenticationPrincipal AppUserDetails userDetails,
                              @Validated(OnCreate.class) @ModelAttribute("matchForm") MatchFormDTO form, BindingResult br, Model model,
                              RedirectAttributes redirectAttributes){
        if(br.hasErrors()) {
            prepareFormModel(model, form, "/matches/create");
            return "/matches/form";
        }
    }


    private void prepareFormModel(Model model, MatchFormDTO form, String formAction) {
        model.addAttribute("matchForm", form);
        model.addAttribute("formAction", formAction);
    }
}

