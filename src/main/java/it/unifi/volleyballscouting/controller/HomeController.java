package it.unifi.volleyballscouting.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller per le pagine di ingresso: landing pubblica, login e home autenticata.
 * Queste rotte erano referenziate da SecurityConfig ma non avevano alcun handler.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model){
        boolean isCoach = false;
        String username = "utente";
        if (authentication != null) {
            username = authentication.getName();
            for (GrantedAuthority a : authentication.getAuthorities()) {
                if ("ROLE_COACH".equals(a.getAuthority())) {
                    isCoach = true;
                }
            }
        }
        model.addAttribute("username", username);
        model.addAttribute("isCoach", isCoach);
        return "home";
    }
}
