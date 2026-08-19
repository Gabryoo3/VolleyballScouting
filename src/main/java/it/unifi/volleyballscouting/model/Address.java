package it.unifi.volleyballscouting.model;

import it.unifi.volleyballscouting.dto.AddressFormDto;
import jakarta.persistence.*;

@Entity
public class Address extends BaseModel{

    //attributes
    private String street; //don't add "via" or "corso"
    private String city;
    private String zipCode;

    protected Address() {}

    //constructor
    public Address(String street, String city, String zipCode) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }
    //methods


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String postalCode) {
        this.zipCode = postalCode;
    }

    public void updateFromDto(AddressFormDto form){
        if (form != null){
            this.street = form.street();
            this.city = form.city();
            this.zipCode = form.zipCode();
        }
    }

}
