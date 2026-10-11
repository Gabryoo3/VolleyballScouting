package it.unifi.volleyballscouting.dto;

import it.unifi.volleyballscouting.model.Address;
import it.unifi.volleyballscouting.model.Match;
import it.unifi.volleyballscouting.model.Referee;
import it.unifi.volleyballscouting.model.Team;
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
        Referee r2,
        @NotNull(message = "La partita deve avere un indirizzo")
        Address address,
        @NotNull(message = "La partita deve avere una data")
        @Future(message = "La partita deve essere assegnata nel futuro")
        LocalDateTime matchDateTime,
        @NotNull(message = "Non è possibile lasciare il campo vuoto")
        @Range(message = "Il numero di set vinti deve variare tra 0 e 3", min = 0, max = 3)
        Integer homeScore,
        @NotNull(message = "Il numero di set vinti non può essere vuoto")
        @Range(message = "Il numero di set vinti deve variare tra 0 e 3", min = 0, max = 3)
        Integer guestScore)
        implements Serializable {
        public static MatchFormDTO fromEntity(Match m){
          return new MatchFormDTO(
                  m.getTeamHome(),
                  m.getTeamGuest(),
                  m.getR1(),
                  m.getR2(),
                  m.getAddress(),
                  m.getMatchDateTime(),
                  m.getHomeScore(),
                  m.getGuestScore()
          );
        }


}