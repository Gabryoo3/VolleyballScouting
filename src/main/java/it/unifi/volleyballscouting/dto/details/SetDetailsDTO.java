package it.unifi.volleyballscouting.dto.details;

import java.io.Serializable;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.GameSet}
 */
public record SetDetailsDTO(int setNumber, int teamHomePoints, int teamGuestPoints) implements Serializable {
}