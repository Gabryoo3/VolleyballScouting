package it.unifi.volleyballscouting.dto;

import it.unifi.volleyballscouting.model.Match;
import it.unifi.volleyballscouting.validation.OnCreate;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Match}
 */
public record MatchFormDTO(
        @NotNull(message = "Il team di casa non può essere vuoto")
        UUID teamHomeId,
        @NotNull(message = "Il team ospite non può essere vuoto")
        UUID teamGuestId,
        @NotNull(message = "L'arbitro deve essere assegnato")
        UUID refereeId,
        @NotNull(message = "La partita deve avere una data")
        @Future(message = "La partita deve essere assegnata nel futuro", groups = OnCreate.class)
        LocalDateTime matchDateTime)
        implements Serializable {
        public static MatchFormDTO fromEntity(Match m){
          return new MatchFormDTO(
                  m.getTeamHome().getId(),
                  m.getTeamGuest().getId(),
                  m.getReferee().getId(),
                  m.getMatchDateTime()
          );
        }
        public static MatchFormDTO empty(){return new MatchFormDTO(null, null, null, null);}


}