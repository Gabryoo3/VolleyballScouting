package it.unifi.volleyballscouting.dto.details;

import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Team;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Coach}
 */
public record CoachDetailsDTO(UUID coachId, String name, String surname,
							  LocalDate birthdate, String phone,
							  String email, boolean owner, UUID teamId, String teamName)
	implements Serializable {

	public static CoachDetailsDTO revealDetails(Coach c, boolean owner, boolean showContacts){
		Team t = c.getTeam();
		return new CoachDetailsDTO(c.getId(), c.getName(), c.getSurname(), owner  ? c.getBirthdate() : null, showContacts ? c.getPhone() : null, showContacts ? c.getEmail() : null,
				owner, t != null ? t.getId() : null,  t != null ? t.getName() : null);
	}
}
