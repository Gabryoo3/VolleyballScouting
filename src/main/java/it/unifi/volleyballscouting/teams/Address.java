package it.unifi.volleyballscouting.teams;

public class Address {

    private String street; //don't add "via" or "corso"
    private String city;
    private String postalCode;

    public Address(String street, String city, String postalCode) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
    }

}
