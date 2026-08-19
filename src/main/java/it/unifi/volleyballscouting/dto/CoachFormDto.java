package it.unifi.volleyballscouting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Coach}
 */
public record CoachFormDto(@NotBlank(message = "Il nome è obbligatorio")
                            String name,
                            @NotBlank(message = "Il cognome è obbligatorio")
                            String surname,
                            @NotBlank(message = "Lo username è obbligatorio")
                            String username,
                            @Size(message = "La password è deve avere almeno 6 caratteri", min = 6)
                            @NotBlank(message = "La password è obbligatoria")
                            String password,
                            @NotNull(message = "La data di nascita è obbligatoria")
                            Date birthdate,
                            @NotBlank(message = "Il telefono è obbligatorio")
                            String phone,
                            @Email(message = "Non è stata inserita una mail valida")
                            @NotBlank(message = "Il campo mail è obbligatorio")
                            String email) implements Serializable {
}