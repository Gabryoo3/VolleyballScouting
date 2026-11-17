package it.unifi.volleyballscouting.dto.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link it.unifi.volleyballscouting.dto.UserAccount}
 */
public record ChangePasswordFormDTO(
		@NotBlank(message = "La password attuale non può essere vuota")
		String oldPassword,
		@NotBlank(message = "La nuova password non può essere vuota")
		@Size(min = 6, message = "La nuova password deve essere di almeno 6 caratteri")
		String newPassword,
		@NotBlank(message = "Il campo ripeti password non può essere vuoto")
		String repeatNewPassword
) implements Serializable {
	public static ChangePasswordFormDTO empty() { return new ChangePasswordFormDTO(null, null, null);}
}