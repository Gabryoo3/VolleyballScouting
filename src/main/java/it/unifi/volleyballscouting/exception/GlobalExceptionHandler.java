package it.unifi.volleyballscouting.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException e, RedirectAttributes ra, HttpServletRequest req){
        ra.addFlashAttribute("errorMessage", e.getMessage());
        String referer = req.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
    @ExceptionHandler(BusinessRuleException.class)
    public String businessRule(BusinessRuleException be, RedirectAttributes ra, HttpServletRequest req){
        ra.addFlashAttribute("errorMessage", be.getMessage());
        String referer = req.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
