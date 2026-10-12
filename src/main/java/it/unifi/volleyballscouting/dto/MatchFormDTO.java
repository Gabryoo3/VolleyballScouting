package it.unifi.volleyballscouting.dto;

import it.unifi.volleyballscouting.model.Address;
import it.unifi.volleyballscouting.model.Match;
import it.unifi.volleyballscouting.model.Referee;
import it.unifi.volleyballscouting.model.Team;
import it.unifi.volleyballscouting.validation.OnCreate;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Match}
 */
public record MatchFormDTO(
        @NotNull(message = "Il team di casa non può essere vuoto")
        Team teamHome,
        @NotNull(message = "Il team ospite non può essere vuoto")
        Team teamGuest,
        Referee r1,
        @NotNull(message = "La partita deve avere un indirizzo")
        Address address,
        @NotNull(message = "La partita deve avere una data")
        @Future(message = "La partita deve essere assegnata nel futuro", groups = OnCreate.class)
        LocalDateTime matchDateTime)
        implements Serializable {
        public static MatchFormDTO fromEntity(Match m){
          return new MatchFormDTO(
                  m.getTeamHome(),
                  m.getTeamGuest(),
                  m.getR1(),
                  m.getAddress(),
                  m.getMatchDateTime()
          );
        }


}