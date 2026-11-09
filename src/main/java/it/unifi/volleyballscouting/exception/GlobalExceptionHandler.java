package it.unifi.volleyballscouting.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;

//Added for managing loops and show errors
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ResourceNotFoundException.class,BusinessRuleException.class} )
    public String handleNotFound(RuntimeException e, RedirectAttributes ra, HttpServletRequest req){
        ra.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:" + safeBack(req);
    }
    private String safeBack(HttpServletRequest req){
        String referer = req.getHeader("Referer");
        if(referer == null) return "/";
        try {
            URI uri = URI.create(referer);
            if (!req.getServerName().equals(uri.getHost())) return "/"; //coming from other website
            String path = uri.getRawPath();
            if (path == null || path.equals(req.getRequestURI())) return "/"; //looping
            return uri.getRawQuery() == null ? path : path + "?" + uri.getRawQuery();
        }catch (IllegalArgumentException ex){
            return "/";
        }
    }
}
