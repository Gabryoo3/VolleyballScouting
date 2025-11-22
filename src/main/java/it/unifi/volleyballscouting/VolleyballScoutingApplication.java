package it.unifi.volleyballscouting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@SpringBootApplication
@RestController
public class VolleyballScoutingApplication{

    @GetMapping("/")
    public String home() {
        return "Hello, Volleyball Scouting!";
    }

    public static void main(String[] args) {
        SpringApplication.run(VolleyballScoutingApplication.class, args);
    }
}
