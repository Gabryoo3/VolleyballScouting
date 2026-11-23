package it.unifi.volleyballscouting.controller;


import it.unifi.volleyballscouting.dto.scouting.ScoutPageDTO;
import it.unifi.volleyballscouting.dto.scouting.ScoutSetFormDTO;
import it.unifi.volleyballscouting.exception.BusinessRuleException;
import it.unifi.volleyballscouting.security.AppUserDetails;
import it.unifi.volleyballscouting.service.PerformanceService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/matches/{matchId}/scout")
public class MatchScoutWebController {

	private final PerformanceService performanceService;

	public MatchScoutWebController(PerformanceService ps){
		this.performanceService = ps;
	}
	@GetMapping
	public String showScoutPage(@AuthenticationPrincipal AppUserDetails userDetails, @PathVariable UUID matchId, Model model){

		ScoutPageDTO page = performanceService.getScoutPage(matchId, userDetails.getId());
		prepareFormModel(model, page, ScoutSetFormDTO.initRoster(page.roster()), page.setNumber());
		return "scout/matchScouting";
	}
	@PostMapping("/sets/{setNumber}")
	public String sendSet(@PathVariable UUID matchId, @PathVariable int setNumber,
						  @Valid @ModelAttribute ("scoutForm") ScoutSetFormDTO form, BindingResult br,
						  @AuthenticationPrincipal AppUserDetails userDetails, Model model, RedirectAttributes ra){
		if (br.hasErrors()) {
			prepareFormModel(model, performanceService.getScoutPage(matchId, userDetails.getId()), form, setNumber);
			return "scout/matchScouting";
		}
		try{
			performanceService.saveSetScouting(matchId, userDetails.getId(), setNumber, form);
		}catch (BusinessRuleException e){
			br.reject("scoutRules", e.getMessage());
			prepareFormModel(model, performanceService.getScoutPage(matchId, userDetails.getId()), form, setNumber);
			return "scout/matchScouting";
		}
		ra.addFlashAttribute("successMessage", "Statistiche del set " + setNumber + " inviate");
		return "redirect:/matches/" + matchId + "/scout";
	}

	private void prepareFormModel(Model model, ScoutPageDTO page, ScoutSetFormDTO form, int setNumber) {
		model.addAttribute("scout", page);
		model.addAttribute("scoutForm", form);
		model.addAttribute("formAction", "/matches/" + page.matchId() + "/scout/sets/" + setNumber);
	}

}
