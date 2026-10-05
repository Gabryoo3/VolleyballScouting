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
        // 1. Creazione Admin demo
        if (adminRepository.findByUsername(adminUsername).isEmpty()) {
            Admin admin = new Admin("Brambilla", "Fumagalli", adminUsername, passwordEncoder.encode(adminPassword));
            adminRepository.save(admin);
        }

        // 2. Primo Coach demo (Firenze Volley)
        if (coachRepository.findByUsername("coach").isEmpty()) {
            Address address = new Address("Via dello Sport 10", "Firenze", "50100");

            Coach coach = new Coach("Mario", "Rossi", "coach", passwordEncoder.encode("coach123"));
            coach.setEmail("coach@demo.it");
            coach.setBirthdate(LocalDate.EPOCH);
            coachRepository.save(coach);

            Team team = new Team("Firenze Volley", address, coach);
            teamRepository.save(team);

            coach.setTeam(team);
            coachRepository.save(coach);

            savePlayer("Bianchi", "Luca", "lbianchi", 4, PlayerRole.ALZATORE, team, 1998);
            savePlayer("Verdi", "Paolo", "pverdi", 12, PlayerRole.SCHIACCIATORE_OPPOSTO, team, 2000);
        }

        // 3. Secondo Coach demo (Polisportiva Remo Masi - Serie C)
        if (coachRepository.findByUsername("coach2").isEmpty()) {
            Address address2 = new Address("Via Calamandrei 1", "Rufina", "50068");

            Coach coach2 = new Coach("Flavio", "Baccetti", "coach2", passwordEncoder.encode("coach123"));
            coach2.setEmail("coach@remomasi.it");
            coach2.setBirthdate(LocalDate.of(1985, 4, 15));
            coachRepository.save(coach2);

            Team team2 = new Team("Remo Masi Serie C", address2, coach2);
            teamRepository.save(team2);

            coach2.setTeam(team2);
            coachRepository.save(coach2);

            savePlayer("Zaccaria", "Matteo", "mzacca", 7, PlayerRole.SCHIACCIATORE_OPPOSTO, team2, 2001);
            savePlayer("Pinzani", "Pietro", "ppinzani", 10, PlayerRole.LIBERO, team2, 1999);
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