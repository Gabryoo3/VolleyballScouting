package it.unifi.volleyballscouting.dto;

import java.util.UUID;

public record UserAccount(UUID id, String username, String password, String role){}
