package it.unifi.volleyballscouting.dto.scouting;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Performance}
 */
public record ScoutRowDTO(@NotNull UUID playerId,
						  @PositiveOrZero int attackPoint, @PositiveOrZero int attackInPlay,
						  @PositiveOrZero int attackError, @PositiveOrZero int servePoint,
						  @PositiveOrZero int serveInPlay, @PositiveOrZero int serveError,
						  @PositiveOrZero int blockPoint, @PositiveOrZero int blockInPlay,
						  @PositiveOrZero int blockError, @PositiveOrZero int receiveGood,
						  @PositiveOrZero int receiveError, @PositiveOrZero int digGood,
						  @PositiveOrZero int digError) implements Serializable {

	public boolean isEmpty() {
		return attackPoint + attackInPlay + attackError + servePoint + serveInPlay + serveError
				+ blockPoint + blockInPlay + blockError + receiveGood + receiveError + digGood + digError == 0;
	}

	public static ScoutRowDTO zero (UUID playerId){
		return new ScoutRowDTO(playerId, 0,0,0,0,0,0,0,0,0,0,0,0,0);
	}
}

