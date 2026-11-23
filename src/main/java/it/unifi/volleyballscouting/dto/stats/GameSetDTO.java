package it.unifi.volleyballscouting.dto.stats;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.GameSet}
 */
public record GameSetDTO(
        @NotNull(message = "Punti di casa obbligatori") @Min(0) Integer teamHomePoints,
        @NotNull(message = "Punti ospite obbligatori") @Min(0) Integer teamGuestPoints) implements Serializable {
      public static GameSetDTO empty() { return new GameSetDTO(null, null);}
}

