package it.unifi.volleyballscouting.dto.scouting;

import it.unifi.volleyballscouting.dto.PlayerRosterDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ScoutSetFormDTO(@Valid @NotNull List<ScoutRowDTO> rows) {
	public static ScoutSetFormDTO initRoster(List<PlayerRosterDTO> roster){
    	return new ScoutSetFormDTO(roster.stream().map(p -> ScoutRowDTO.zero(p.id())).toList());
	}

}
