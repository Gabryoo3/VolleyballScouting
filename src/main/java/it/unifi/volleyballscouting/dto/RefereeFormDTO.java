package it.unifi.volleyballscouting.dto;

import it.unifi.volleyballscouting.model.Referee;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Referee}
 */
public record RefereeFormDTO(
        @NotBlank(message = "Il nome non può rimanere vuoto")
        String name,
        @NotBlank(message = "Il cognome non può rimanere vuoto")
        String surname,
        @NotBlank(message = "Il nome utente non può rimanere vuoto")
        String username,
        @NotNull(message = "La data di nascita non può rimanere vuota")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate birthdate,
        String phone,
        @Email
        String email
) implements Serializable {
public static RefereeFormDTO fromEntity(Referee r){
        return new RefereeFormDTO(
                r.getName(),
                r.getSurname(),
                r.getUsername(),
                r.getBirthdate(),
                r.getPhone(),
                r.getEmail()
        );
}
public static RefereeFormDTO empty() {return new RefereeFormDTO(null,null,null,null,null,null);}
}