package it.unifi.volleyballscouting.dto;

import it.unifi.volleyballscouting.model.Team;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Team}
 */
public record TeamFormDto(
        @Size(message = "Il nome deve contenere dai 5 ai 99 caratteri", min = 5, max = 99)
        @NotBlank(message = "Il nome non può essere vuoto")
        String name,
        @Valid
        @NotNull(message = "L'indirizzo è necessario")
        AddressFormDto address
        ) implements Serializable {

        public static TeamFormDto empty(){
                return new TeamFormDto("", AddressFormDto.empty());
        }

        public static TeamFormDto fromEntity(Team t){
                return new TeamFormDto(
                        t.getName(),
                        AddressFormDto.fromEntity(t)
                );
        }
}