package it.unifi.volleyballscouting.dto.details;

import it.unifi.volleyballscouting.model.Referee;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Referee}
 */
public record RefereeDetailsDTO(UUID id, String name, String surname, LocalDate birthdate, String phone,
								String email)
		implements Serializable {

	public static RefereeDetailsDTO fullDetails (Referee r){
		return new RefereeDetailsDTO(r.getId(), r.getName(), r.getSurname(), r.getBirthdate(), r.getPhone(), r.getEmail());
	}
}