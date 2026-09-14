package it.unifi.volleyballscouting.config;

import it.unifi.volleyballscouting.model.*;
import it.unifi.volleyballscouting.repository.AdminRepository;
import it.unifi.volleyballscouting.repository.CoachRepository;
import it.unifi.volleyballscouting.repository.PlayerRepository;
import it.unifi.volleyballscouting.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * ATTENZIONE: dati di prova, SOLO PER SVILUPPO/TEST.
 * Crea un allenatore demo con una squadra e due giocatori, ma SOLO se il database
 * non contiene ancora nessun allenatore (quindi non duplica nulla).
 *
 * Credenziali di accesso demo -> username: coach   password: coach123
 *
 * Per disattivarlo: cancellare questo file, oppure commentare l'annotazione @Component qui sotto.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final CoachRepository coachRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final String adminUsername;
    private final String adminPassword;

    public DataSeeder(CoachRepository coachRepository,
                      TeamRepository teamRepository,
                      PlayerRepository playerRepository,
                      PasswordEncoder passwordEncoder,
                      AdminRepository adminRepository,
                      @Value("${app.admin.username}") String adminUsername,
                      @Value("${app.admin.password}") String adminPassword
                      ) {
        this.coachRepository = coachRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (adminRepository.findByUsername(adminUsername).isEmpty()){
            Admin admin = new Admin("Brambilla", "Fumagalli", adminUsername, passwordEncoder.encode(adminPassword));
            adminRepository.save(admin);
        }
        if (coachRepository.findByUsername("coach").isEmpty()) {

        Address address = new Address("Via dello Sport 10", "Firenze", "50100");

        Coach coach = new Coach("Mario", "Rossi", "coach", null);
        coach.setPassword(passwordEncoder.encode("coach123"));
        coach.setEmail("coach@demo.it");
        coachRepository.save(coach);

        Team team = new Team("Firenze Volley", address, coach);
        teamRepository.save(team);

        coach.setTeam(team);
        coachRepository.save(coach);

        savePlayer("Bianchi", "Luca", "lbianchi", 4, PlayerRole.ALZATORE, team, 1998);
        savePlayer("Verdi", "Paolo", "pverdi", 12, PlayerRole.SCHIACCIATORE_OPPOSTO, team, 2000);
    }
    }

    private void savePlayer(String surname, String name, String username, int number,
                            PlayerRole role, Team team, int birthYear) {
        LocalDate birthdate = LocalDate.of(birthYear, 1, 1);
        Player player = new Player(surname, name, username,
                passwordEncoder.encode("player123"), birthdate, number, role);
        player.setTeam(team);
        playerRepository.save(player);
    }
}
