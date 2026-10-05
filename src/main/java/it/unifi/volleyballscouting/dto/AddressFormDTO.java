package it.unifi.volleyballscouting.dto;

import it.unifi.volleyballscouting.model.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Address}
 */
public record AddressFormDTO(
        @NotBlank(message = "La via non può essere vuota")
        String street,
        @NotBlank(message = "La città non può essere vuota")
        String city,
        @NotBlank
        @Size(min = 5, max = 5, message = "Il codice postale deve essere di 5 cifre")
        String zipCode) implements Serializable {

        public static AddressFormDTO empty(){
                return new AddressFormDTO("", "", "");
        }

        public static AddressFormDTO fromEntity(Team t){
                return new AddressFormDTO(
                        t.getAddress().getStreet(),
                        t.getAddress().getCity(),
                        t.getAddress().getZipCode()
                );
        }
}