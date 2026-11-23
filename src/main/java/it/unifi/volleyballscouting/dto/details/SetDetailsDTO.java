package it.unifi.volleyballscouting.dto.details;

import java.io.Serializable;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.GameSet}
 */
public record SetDetailsDTO(int teamHomePoints, int teamGuestPoints, int setNumber) implements Serializable {
}