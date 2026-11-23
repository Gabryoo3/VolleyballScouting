package it.unifi.volleyballscouting.dto.details;

import it.unifi.volleyballscouting.model.Address;
import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Team;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Team}
 */
public record TeamDetailsDTO(UUID id, String name, String addressStreet, String addressCity, String addressZipCode,
                             UUID coachId, String coachName, boolean editable)
		implements Serializable {
	public static TeamDetailsDTO showDetails(Team t, boolean editable){
		Coach c = t.getCoach();
		Address a = t.getAddress();
		return new TeamDetailsDTO(t.getId(), t.getName(),
				a != null ? a.getStreet() : null, a != null ? a.getCity() : null, a != null ? a.getZipCode() : null,
				c != null ? c.getId() : null, c != null ? c.getName() + " " + c.getSurname() : null, editable);
	}
}