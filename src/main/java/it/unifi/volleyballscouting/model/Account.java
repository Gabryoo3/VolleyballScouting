package it.unifi.volleyballscouting.model;

import java.util.UUID;

public interface Account {
    UUID getId();
    String getUsername();
    String getPassword();
    void setPassword(String password);
}
