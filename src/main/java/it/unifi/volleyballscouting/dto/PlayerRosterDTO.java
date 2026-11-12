package it.unifi.volleyballscouting.dto;

import it.unifi.volleyballscouting.model.Player;
import it.unifi.volleyballscouting.model.PlayerRole;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Player}
 */
public record PlayerRosterDTO(UUID id, String name, String surname, Integer number, PlayerRole role) implements Serializable {

	public static PlayerRosterDTO of(Player p){
		return new PlayerRosterDTO(p.getId(), p.getName(), p.getSurname(), p.getNumber(), p.getRole());
	}
}