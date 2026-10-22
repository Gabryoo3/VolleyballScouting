package it.unifi.volleyballscouting.config;

import java.util.UUID;

public interface Account {
    UUID getId();
    String getUsername();
    String getPassword();
    void setPassword(String password);
}
