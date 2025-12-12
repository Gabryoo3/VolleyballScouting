package it.unifi.volleyballscouting.teams;

import it.unifi.volleyballscouting.match.Set;
import jakarta.persistence.*;
@Entity
public class Performance {

    @Id
    @ManyToOne
    private Player player;
    @ManyToOne
    private Set set;


}
