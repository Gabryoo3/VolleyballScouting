package it.unifi.volleyballscouting.dto.details;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Match}
 */
public record MatchDetailsDTO(UUID matchId, UUID teamHomeId, String teamHomeName, UUID teamGuestId, String teamGuestName,
							  String refereeName, String refereeSurname, String addressStreet, String addressCity,
							  String addressZipCode, LocalDateTime matchDateTime, Integer homeScore,
							  Integer guestScore, boolean started, boolean finished, List<SetDetailsDTO> sets, boolean canScout) implements Serializable {
}