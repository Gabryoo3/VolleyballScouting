package it.unifi.volleyballscouting.model;



import jakarta.persistence.*;

@Entity
public class Address {
    //attributes
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private String street; //don't add "via" or "corso"
    private String city;
    private String postalCode;

    //constructor
    public Address(String street, String city, String postalCode) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
    }
    protected Address() {}

    //methods
}
