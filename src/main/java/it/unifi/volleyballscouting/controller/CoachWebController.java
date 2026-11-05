package it.unifi.volleyballscouting.controller;

import it.unifi.volleyballscouting.dto.CoachFormDTO;
import it.unifi.volleyballscouting.exception.BusinessRuleException;
import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.security.AppUserDetails;
import it.unifi.volleyballscouting.service.CoachService;

import it.unifi.volleyballscouting.validation.OnCreate;
import it.unifi.volleyballscouting.validation.OnUpdate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.UUID;

@Controller
@RequestMapping("/coaches")
public class CoachWebController {

    private final CoachService coachService;

    public CoachWebController(CoachService cs){
        this.coachService = cs;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        prepareFormModel(model, CoachFormDTO.empty(), "/coaches/register");
        return "coaches/form";
    }
    @PostMapping("/register")
    public String registerCoach(@Validated(OnCreate.class) @ModelAttribute("coachForm") CoachFormDTO form,
                             BindingResult br, Model model, RedirectAttributes ra){
        if (form.password() != null && !form.password().equals(form.confirmPassword())){
            br.rejectValue("confirmPassword", "mismatch", "Le password non corrispondono");
            prepareFormModel(model, form, "/coaches/register");
            return "coaches/form";
        }
        if (br.hasErrors()){
            prepareFormModel(model, form,"/coaches/register");
            return "coaches/form";
        }
        try{
            coachService.save(form);
        }catch(BusinessRuleException e){
            br.reject("coachRules", e.getMessage());
            prepareFormModel(model,form,"/coaches/register");
            return "coaches/form";
        }
        ra.addFlashAttribute("successMessage", "Registrazione completata!");
        return "redirect:/login";
    }
    @GetMapping("/{coachId}/edit")
    public String showEditForm(@PathVariable UUID coachId, @AuthenticationPrincipal AppUserDetails userDetails, Model model){

        Coach c = coachService.findOwnedBy(coachId, userDetails.getId());
        prepareFormModel(model, CoachFormDTO.fromEntity(c), "/coaches/" + coachId + "/update");
        return "coaches/form";
    }
    @PostMapping("/{coachId}/update")
    public String updateCoach(@AuthenticationPrincipal AppUserDetails userDetails,
                              @PathVariable UUID coachId,
    @Validated(OnUpdate.class) @ModelAttribute("coachForm") CoachFormDTO form,
                              BindingResult br, Model model,
                              RedirectAttributes ra){
        if(br.hasErrors()){
            prepareFormModel(model, form, "/coaches/" + coachId + "/update");
            return "coaches/form";
        }
        try{
            coachService.updateCoach(coachId, userDetails.getId(), form);
        }catch(BusinessRuleException e){
            br.reject("coachRules", e.getMessage());
            prepareFormModel(model, form, "/coaches/" + coachId + "/update");
            return "coaches/form";
        }
        ra.addFlashAttribute("successMessage", "Allenatore modificato con successo!");
        return "redirect:/coaches/" + coachId + "/details";
    }


    private void prepareFormModel(Model model, CoachFormDTO form, String formAction){
        model.addAttribute("coachForm", form);
        model.addAttribute("formAction", formAction);
    }

}
