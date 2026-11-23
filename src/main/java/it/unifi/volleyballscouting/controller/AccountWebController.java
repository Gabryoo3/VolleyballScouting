package it.unifi.volleyballscouting.controller;

import it.unifi.volleyballscouting.dto.forms.ChangePasswordFormDTO;
import it.unifi.volleyballscouting.exception.BusinessRuleException;
import it.unifi.volleyballscouting.security.AppUserDetails;
import it.unifi.volleyballscouting.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
public class AccountWebController {

	private final UserAccountService userAccountService;

	public AccountWebController(UserAccountService uas){
		this.userAccountService = uas;
	}
	@GetMapping("/changepassword")
	public String showEditPasswordForm (Model model){
    	prepareFormModel(model, ChangePasswordFormDTO.empty(), "/account/changePassword");
		return "account/change_password";
	}

	@PostMapping("/changepassword")
		public String setNewPassword(@AuthenticationPrincipal AppUserDetails userDetails,
									 @Valid @ModelAttribute("changePasswordForm") ChangePasswordFormDTO form,
									 BindingResult br, Model model, RedirectAttributes ra){
		if(br.hasErrors()){
			prepareFormModel(model, form, "/account/changePassword");
			return "account/change_password";
		}
		try{
			userAccountService.changePassword(userDetails.getId(), form.oldPassword(), form.newPassword(), form.repeatNewPassword());
		}catch(BusinessRuleException e){
			br.reject("changePasswordRules", e.getMessage());
			prepareFormModel(model, form, "/account/changePassword");
			return "account/change_password";
		}
		ra.addFlashAttribute("successMessage", "Password cambiata con successo!");
		return "redirect:/home";
	}


	private void prepareFormModel(Model model, ChangePasswordFormDTO form, String formAction){
		model.addAttribute("changePasswordForm", form);
		model.addAttribute("formAction", formAction);
	}
}
