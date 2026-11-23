package it.unifi.volleyballscouting.dto.details;

import it.unifi.volleyballscouting.model.Player;
import it.unifi.volleyballscouting.model.PlayerRole;
import it.unifi.volleyballscouting.model.Team;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Player}
 */
public record PlayerDetailsDTO(UUID playerId, String name, String surname, LocalDate birthdate, Integer number,
							   PlayerRole role, String phone, String email, UUID teamId, String teamName, boolean editable)
	implements Serializable {

	public static PlayerDetailsDTO revealDetails(Player p, boolean editable, boolean showContacts) {
		Team t = p.getTeam();
		return new PlayerDetailsDTO(p.getId(), p.getName(), p.getSurname(), p.getBirthdate(),
				p.getNumber(), p.getRole(), showContacts ? p.getPhone() : null,
				showContacts ? p.getEmail() : null, t != null ? t.getId(): null, t != null ? t.getName() : null, editable);
	}


}