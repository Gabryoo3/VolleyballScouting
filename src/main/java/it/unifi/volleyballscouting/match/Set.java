package it.unifi.volleyballscouting.match;

import jakarta.persistence.*;

@Entity
public class Set {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    Match match;
}
