package it.unifi.volleyballscouting.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Team extends BaseModel{
    //attributes
    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(length = 100)
    private String name;
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    //constructor
    public Team(){}
    public Team(String name, Address address) {
        this.name = name;
        this.address=address;
    }
    //methods
    public Address getAddress(){
        return address;
    }
    public void setAddress(Address a){address=a;}
    public String getName() {return name;}
    public void setName(String n){name=n;}

    public void addPlayer(Player player, String role){
        players.add(player);
        player.setTeam(this);
        player.setRole(role);
    }
    public void removePlayer(Player player){
        player.setTeam(null);
        player.setRole(null);
        players.remove(player);
    }
}