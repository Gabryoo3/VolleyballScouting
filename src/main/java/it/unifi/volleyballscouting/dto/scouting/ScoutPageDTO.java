package it.unifi.volleyballscouting.dto.scouting;

import it.unifi.volleyballscouting.dto.PlayerRosterDTO;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Match}
 */
public record ScoutPageDTO(UUID matchId, String teamHomeName, String teamGuestName, String teamName,
						   int setNumber, boolean alreadySent, List<PlayerRosterDTO> roster) implements Serializable { }