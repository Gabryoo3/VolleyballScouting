package it.unifi.volleyballscouting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// FIX: rimosso @RestController e il vecchio endpoint di prova @GetMapping("/")
// (restituiva "Hello, Volleyball Scouting!") che andava in conflitto con la
// landing page di HomeController. La classe main non deve essere un controller.
@SpringBootApplication
public class VolleyballScoutingApplication {

    public static void main(String[] args) {
        SpringApplication.run(VolleyballScoutingApplication.class, args);
    }
}
