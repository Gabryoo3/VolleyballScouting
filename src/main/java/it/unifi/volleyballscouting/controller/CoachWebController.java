package it.unifi.volleyballscouting.controller;

import it.unifi.volleyballscouting.dto.CoachFormDto;
import it.unifi.volleyballscouting.dto.PlayerFormDto;
import it.unifi.volleyballscouting.model.Player;
import it.unifi.volleyballscouting.model.PlayerRole;
import it.unifi.volleyballscouting.repository.CoachRepository;
import it.unifi.volleyballscouting.repository.PlayerRepository;
import it.unifi.volleyballscouting.service.CoachService;
import it.unifi.volleyballscouting.service.TeamService;
import it.unifi.volleyballscouting.validation.OnCreate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.Period;

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
        prepareFormModel(model, CoachFormDto.empty());
        return "coaches/form";
    }
    @PostMapping("/register")
    public String creatCoach(@Validated(OnCreate.class) @ModelAttribute("coachForm") CoachFormDto form,
                             BindingResult br, Model model, RedirectAttributes ra){
        if (br.hasErrors()){
            prepareFormModel(model, form);
            return "coaches/form";
        }
        if(cr.findByUsername(form.username()).isPresent() ||
                pr.findByUsername(form.username()).isPresent()){
            br.rejectValue("username", "duplicate", "L'username è già in uso");
            prepareFormModel(model, form);
            return "players/form";
        }
        if (form.birthdate() != null){
           LocalDate birth = form.birthdate();
           if(Period.between(birth, LocalDate.now()).getYears() < 18) {
               br.rejectValue("birthdate", "underage", "L'allenatore deve essere maggiorenne");
               prepareFormModel(model, form);
               return "coaches/form";
           }
        }
        if (!form.password().equals(form.confirmPassword())) {
            br.rejectValue("password", "mismatch", "Le password non corrispondono");
            prepareFormModel(model, form);
            return "coaches/form";
        }
        //TODO: complete CoachService with the needed methods, like save
    }


    private void prepareFormModel(Model model, CoachFormDto form){
        model.addAttribute("coachForm", form);
    }

}
