package it.unifi.volleyballscouting.model;

import jakarta.persistence.*;

@Entity
public class Address extends BaseModel {

    //attributes
    private String street; //don't add "via" or "corso"
    private String city;
    private String postalCode;

    protected Address() {}

    //constructor
    public Address(String street, String city, String postalCode) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


}
