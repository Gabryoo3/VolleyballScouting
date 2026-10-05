package it.unifi.volleyballscouting.dto;

import it.unifi.volleyballscouting.model.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Address}
 */
public record AddressFormDTO(
        @NotBlank(message = "La via non può essere vuota")
        String street,
        @NotBlank(message = "La città non può essere vuota")
        String city,
        @NotBlank(message = "Il CAP non può essere vuoto")
        @Pattern(regexp = "\\d{5}")
        String zipCode) implements Serializable {

        public static AddressFormDTO empty(){
                return new AddressFormDTO("", "", "");
        }

        public static AddressFormDTO fromEntity(Address a){
                return new AddressFormDTO(
                        a.getStreet(),
                        a.getCity(),
                        a.getZipCode()
                );
        }
}