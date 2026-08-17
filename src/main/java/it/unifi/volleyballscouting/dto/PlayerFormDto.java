package it.unifi.volleyballscouting.dto;

import it.unifi.volleyballscouting.model.Player;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Player}
 */
public record PlayerFormDto(
        @NotBlank(message = "Il nome è necessario")
        String name,
        @NotBlank(message = "Il cognome è necessario")
        String surname,
        @NotBlank(message = "Il nome utente è necessario")
        String username,
        @NotBlank(message = "La data di nascita è necessaria")
        Date birthdate,
        @NotBlank(message = "Il numero del giocatore è necessario")
        @Min(value = 1, message = "Il numero deve essere almeno 1")
        @Max(value = 99, message = "Il numero deve essere almeno 99")
        Integer number,
        @NotBlank(message = "Il ruolo del giocatore è necessario")
        String role,
        String phone,
        String email) implements Serializable {
    public static PlayerFormDto fromEntity(Player p){
        return new PlayerFormDto(
                p.getName(),
                p.getSurname(),
                p.getUsername(),
                p.getBirthdate(),
                p.getNumber(),
                p.getRole(),
                p.getPhone(),
                p.getEmail()
        );
    }
}
