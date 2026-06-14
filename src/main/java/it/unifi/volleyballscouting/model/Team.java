package it.unifi.volleyballscouting.model;

import jakarta.persistence.*;

@Entity
public class Team {
    //attributes
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Address address;
    @Column(length = 100)
    private String nome;


    //constructor
    public Team(Address address){
        this.address=address;
    }
    public Team(){}

    //methods
    public Address getAddress(){
        return address;
    }
    public void setAddress(Address a){address=a;}
    public String getNome() {return nome;}
    public void setNome(String n){
        nome=n;
    }
}