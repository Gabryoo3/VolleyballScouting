package it.unifi.volleyballscouting.dto;

import it.unifi.volleyballscouting.validation.OnCreate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Coach}
 */
public record CoachFormDto(@NotBlank(message = "Il nome è obbligatorio")
                            String name,
                            @NotBlank(message = "Il cognome è obbligatorio")
                            String surname,
                            @NotBlank(message = "Lo username è obbligatorio")
                            String username,
                            @Size(message = "La password è deve avere almeno 6 caratteri", min = 6, groups = OnCreate.class)
                            @NotBlank(message = "La password è obbligatoria", groups = OnCreate.class)
                            String password,
                            @NotBlank(message = "La conferma password è obbligatoria", groups = OnCreate.class)
                            String confirmPassword,
                            @NotNull(message = "La data di nascita è obbligatoria")
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                            LocalDate birthdate,
                            String phone,
                            @Email(message = "Non è stata inserita una mail valida")
                            String email) implements Serializable {
    public static CoachFormDto empty(){
        return new CoachFormDto("", "", "", "","",null,"", "");
    }
}