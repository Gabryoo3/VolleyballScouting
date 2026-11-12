package it.unifi.volleyballscouting.controller;

import it.unifi.volleyballscouting.dto.stats.GameSetDTO;
import it.unifi.volleyballscouting.exception.BusinessRuleException;
import it.unifi.volleyballscouting.model.Match;
import it.unifi.volleyballscouting.security.AppUserDetails;
import it.unifi.volleyballscouting.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class MatchPlayWebController {
    private final MatchService matchService;

    public MatchPlayWebController(MatchService ms){
        this.matchService = ms;
    }

    @GetMapping("/referees/matches")
    public String refereeMatches(@AuthenticationPrincipal AppUserDetails user, Model model){
        model.addAttribute("matches", matchService.findAssignedTo(user.getId()));
        return "referee/matches"; //the route is already covered by SecurityConfig, no need of double check
    }
    @GetMapping("/matches/{matchId}/play")
    public String playForm(@PathVariable UUID matchId, @AuthenticationPrincipal AppUserDetails userDetails, Model model){
        prepareFormModel(model, matchId, userDetails.getId(), GameSetDTO.empty());
        return "referee/play";
    }
    @PostMapping("/matches/{matchId}/play")
    public String play(@PathVariable UUID matchId, @AuthenticationPrincipal AppUserDetails user, RedirectAttributes ra){
        try{
            matchService.start(matchId, user.getId());
        }catch(BusinessRuleException e){
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/matches/" + matchId + "/play";
        }
        ra.addFlashAttribute("successMessage", "Match avviato con successo!");
        return "redirect:/matches/" + matchId + "/play";
    }
    @PostMapping("/matches/{matchId}/sets/{setNumber}/close")
    public String closeSet(@PathVariable UUID matchId, @PathVariable int setNumber,
                           @Valid @ModelAttribute ("scoreForm") GameSetDTO form, BindingResult br,
                           @AuthenticationPrincipal AppUserDetails userDetails,
                           Model model, RedirectAttributes ra){
        if (br.hasErrors()){
            prepareFormModel(model, matchId, userDetails.getId(), form);
            return "referee/play";
        }
        try{
            matchService.closeSet(matchId, userDetails.getId(), setNumber, form.teamHomePoints(), form.teamGuestPoints());
        }catch (BusinessRuleException e){
            br.reject("setRules", e.getMessage());
            prepareFormModel(model, matchId, userDetails.getId(), form);
            return "referee/play";
        }
        ra.addFlashAttribute("successMessage", "Set " + setNumber + " chiuso");
        return "redirect:/matches/" + matchId + "/play";
    }

    private void prepareFormModel(Model model, UUID matchId, UUID refereeId, GameSetDTO form) {
        Match m = matchService.findRefereedBy(matchId, refereeId);
        model.addAttribute("match", m);
        model.addAttribute("currentSet", m.getCurrentSet());
        model.addAttribute("scoreForm", form);
    }


}
