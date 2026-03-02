package it.unifi.volleyballscouting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootApplication
@Controller

public class VolleyballScoutingApplication{

    @GetMapping("/")
    public String home() {
        return "index.html"; //Uso del ResponseBody restituisce quello specificato nella stringa, senza riporta la pagina
        //della stringa
    }
    public static void main(String[] args) {
        SpringApplication.run(VolleyballScoutingApplication.class, args);
    }

}
