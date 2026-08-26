package it.unifi.volleyballscouting.config;

import it.unifi.volleyballscouting.model.Address;
import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Player;
import it.unifi.volleyballscouting.model.PlayerRole;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.repository.AddressRepository;
import it.unifi.volleyballscouting.repository.CoachRepository;
import it.unifi.volleyballscouting.repository.PlayerRepository;
import it.unifi.volleyballscouting.repository.TeamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

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
    private final AddressRepository addressRepository;
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(CoachRepository coachRepository,
                      TeamRepository teamRepository,
                      AddressRepository addressRepository,
                      PlayerRepository playerRepository,
                      PasswordEncoder passwordEncoder) {
        this.coachRepository = coachRepository;
        this.teamRepository = teamRepository;
        this.addressRepository = addressRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (coachRepository.findByUsername("coach").isPresent()) {
            return; // l'allenatore demo esiste già: non duplico nulla
        }

        Address address = new Address("Via dello Sport 10", "Firenze", "50100");
        addressRepository.save(address);

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

    private void savePlayer(String surname, String name, String username, int number,
                            PlayerRole role, Team team, int birthYear) {
        Calendar cal = Calendar.getInstance();
        cal.set(birthYear, Calendar.JANUARY, 1, 0, 0, 0);
        Date birthdate = cal.getTime();

        Player player = new Player(surname, name, username,
                passwordEncoder.encode("player123"), birthdate, number, role);
        player.setTeam(team);
        playerRepository.save(player);
    }
}
