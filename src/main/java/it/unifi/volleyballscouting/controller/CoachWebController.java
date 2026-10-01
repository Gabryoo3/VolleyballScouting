package it.unifi.volleyballscouting.controller;

import it.unifi.volleyballscouting.dto.CoachFormDTO;
import it.unifi.volleyballscouting.dto.CoachStatsDTO;
import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.repository.CoachRepository;
import it.unifi.volleyballscouting.repository.PlayerRepository;
import it.unifi.volleyballscouting.security.AppUserDetails;
import it.unifi.volleyballscouting.service.CoachService;
import it.unifi.volleyballscouting.service.TeamService;
import it.unifi.volleyballscouting.validation.OnCreate;
import it.unifi.volleyballscouting.validation.OnUpdate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Controller
@RequestMapping("/coaches")
public class CoachWebController {

    private final CoachService coachService;
    private final TeamService teamService;
    private final CoachRepository cr;
    private final PlayerRepository pr;

    public CoachWebController(CoachService cs, TeamService ts, CoachRepository cr, PlayerRepository pr){
        this.coachService = cs;
        this.teamService = ts;
        this.cr = cr;
        this.pr = pr;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        prepareFormModel(model, CoachFormDTO.empty(),"/coaches/create");
        return "coaches/form";
    }
    @PostMapping("/create")
    public String createCoach(@Validated(OnCreate.class) @ModelAttribute("coachForm") CoachFormDTO form,
                             BindingResult br, Model model, RedirectAttributes ra){
        if (br.hasErrors()){
            prepareFormModel(model, form, "/coaches/create");
            return "coaches/form";
        }
        if(cr.findByUsername(form.username()).isPresent() ||
                pr.findByUsername(form.username()).isPresent()){
            br.rejectValue("username", "duplicate", "L'username è già in uso");
            prepareFormModel(model, form, "/coaches/create");
            return "coaches/form";
        }
        if (form.birthdate() != null){
           LocalDate birth = form.birthdate();
           if(Period.between(birth, LocalDate.now()).getYears() < 18) {
               br.rejectValue("birthdate", "underage", "L'allenatore deve essere maggiorenne");
               prepareFormModel(model, form, "/coaches/create");
               return "coaches/form";
           }
        }
        if (!form.password().equals(form.confirmPassword())) {
            br.rejectValue("password", "mismatch", "Le password non corrispondono");
            prepareFormModel(model, form, "/coaches/create");
            return "coaches/form";
        }
        coachService.save(form);
        ra.addFlashAttribute("successMessage", "Coach creato con successo!");
        return "redirect:/coaches/home";
    }
    @GetMapping("/{coachId}/edit")
    public String showEditForm(@PathVariable UUID coachId, Model model){
        Coach c = coachService.findById(coachId);
        prepareFormModel(model, CoachFormDTO.fromEntity(c), "/coaches/" + coachId + "/update");
        return "coaches/form";
    }
    @PostMapping("/{coachId}/update")
    public String editCoach(@AuthenticationPrincipal AppUserDetails userDetails,
                            @PathVariable UUID coachId,
                            @Validated(OnUpdate.class) @ModelAttribute("coachForm") CoachFormDTO form,
                            BindingResult br, Model model, RedirectAttributes ra){
        if (br.hasErrors()){
            prepareFormModel(model, form, "/coaches/" + coachId + "/update");
            return "coaches/form";
        }
        if(cr.existsByUsernameAndIdNot(form.username(), coachId) || pr.findByUsername(form.username()).isPresent()){
            br.rejectValue("username", "duplicate", "L'username è già in uso");
            prepareFormModel(model, form, "/coaches/" + coachId + "/update");
            return "coaches/form";
        }
        if (form.birthdate() != null && Period.between(form.birthdate(), LocalDate.now()).getYears() < 18){
            br.rejectValue("birthdate", "underage", "L'allenatore deve essere maggiorenne");
            prepareFormModel(model, form, "/coaches/" + coachId + "/update");
            return "coaches/form";
        }
        coachService.updateCoach(coachId, form);
        ra.addFlashAttribute("successMessage", "Allenatore aggiornato con successo!");
        return "redirect:/home";
    }
    @GetMapping("/details/{coachId}")
    public String showCoachDetails(@PathVariable UUID coachId, Model model){
        Coach c = coachService.findById(coachId);
        CoachStatsDTO stats = //TODO: do it after MatchService
    }
    private void prepareFormModel(Model model, CoachFormDTO form, String formAction){
        model.addAttribute("coachForm", form);
        model.addAttribute("formAction", formAction);
    }

}
