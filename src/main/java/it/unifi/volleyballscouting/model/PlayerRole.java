package it.unifi.volleyballscouting.model;

public enum PlayerRole{
    ALZATORE("Alzatore"),
    SCHIACCIATORE_CENTRALE("Schiacciatore Centrale"),
    SCHIACCIATORE_LATERALE_L("Schiacciatore Laterale L"),
    SCHIACCIATORE_LATERALE_R("Schiacciatore Laterale R"),
    SCHIACCIATORE_OPPOSTO("Schiacciatore Opposto"),
    LIBERO("Libero");

    private final String displayName;
    PlayerRole(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}