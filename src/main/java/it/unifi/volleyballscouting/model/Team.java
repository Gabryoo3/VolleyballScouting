package it.unifi.volleyballscouting.model;

import it.unifi.volleyballscouting.dto.TeamFormDto;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Team extends BaseModel{
    //attributes
    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(length = 100, unique = true, nullable = false)
    private String name;
    @OneToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;

    //constructor
    public Team(){}
    public Team(String name, Address address, Coach coach) {
        this.name = name;
        this.address = address;
        this.coach = coach;
        setCreatedAt(LocalDateTime.now());
    }
    //methods
    public Address getAddress(){
        return address;
    }
    public void setAddress(Address a){address=a;}
    public String getName() {return name;}
    public void setName(String n){name=n;}

    public Coach getCoach() {return coach;}

    public void setCoach(Coach coach) {this.coach = coach;}

    public void updateFromDto(TeamFormDto form){
        this.name = form.name();
        if (form.address() != null){
            if (this.address == null){
                this.address = new Address(
                        form.address().street(),
                        form.address().city(),
                        form.address().zipCode()
                );
            }
            else
                this.address.updateFromDto(form.address());
        }
    }

}