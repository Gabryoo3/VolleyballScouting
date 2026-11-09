package it.unifi.volleyballscouting.controller;


import it.unifi.volleyballscouting.dto.RefereeFormDTO;
import it.unifi.volleyballscouting.exception.BusinessRuleException;
import it.unifi.volleyballscouting.model.Referee;
import it.unifi.volleyballscouting.service.RefereeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/referees")
public class RefereeWebController {

    private final RefereeService refereeService;

    public RefereeWebController (RefereeService rs){
        this.refereeService = rs;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        prepareFormModel(model, RefereeFormDTO.empty(), "/referees/create");
        return "referees/form";
    }
    @PostMapping("/create")
    public String createReferee(@Valid @ModelAttribute("refereeForm") RefereeFormDTO form,
                                BindingResult br, Model model,
                                RedirectAttributes ra){
        String tempPassword;
        if (br.hasErrors()){
            prepareFormModel(model, form, "/referees/create");
            return "referees/form";
        }
        try {
            tempPassword = refereeService.save(form);
        }catch(BusinessRuleException e){
            br.reject("refereeRules", e.getMessage());
            prepareFormModel(model, form, "/referees/create");
            return "referees/form";
        }
        ra.addFlashAttribute("tempPassword", tempPassword);
        ra.addFlashAttribute("successMessage", "La creazione arbitro è avvenuta con successo!");
        return "redirect:/referees/list";
    }
    @GetMapping("/{refereeId}/edit")
    public String showEditForm(@PathVariable UUID refereeId,
                                    Model model){
        Referee r = refereeService.findById(refereeId);
        prepareFormModel(model, RefereeFormDTO.fromEntity(r), "/referees/" + refereeId + "/update");
        return "referees/form";
    }
    @PostMapping("/{refereeId}/update")
    public String editReferee(@PathVariable UUID refereeId,
                              @Valid @ModelAttribute("refereeForm") RefereeFormDTO form,
                              BindingResult br,
                              Model model, RedirectAttributes redirectAttributes){
        if(br.hasErrors()){
            prepareFormModel(model, form, "/referees/" + refereeId + "/update");
            return "referees/form";
        }
        try{
            refereeService.updateReferee(refereeId, form);
        } catch (BusinessRuleException e){
            br.reject("refereeRules", e.getMessage());
            prepareFormModel(model, form, "/referees/" + refereeId + "/update");
            return "referees/form";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Arbitro modificato con successo!");
        return "redirect:/referees/" + refereeId + "/details";
    }
    @GetMapping("/list")
    public String listReferee(@RequestParam (required = false) String surname,
                              Model model){
        List<Referee> referees;
        String cleanSurname = (surname != null && !surname.isBlank()) ? surname : null;
        if (cleanSurname != null)
            referees = refereeService.findBySurname(cleanSurname);
        else
            referees = refereeService.findAll();
        model.addAttribute( "referees", referees);
        return "referees/list";
    }
    @GetMapping("/{refereeId}/details")
    public String showRefereeDetails(@PathVariable UUID refereeId, Model model){
        Referee r = refereeService.findById(refereeId);
        model.addAttribute("referee", r);
        return "referees/detail";
    }

    private void prepareFormModel(Model model, RefereeFormDTO form, String formAction){
        model.addAttribute("refereeForm", form);
        model.addAttribute("formAction", formAction);
    }
}
