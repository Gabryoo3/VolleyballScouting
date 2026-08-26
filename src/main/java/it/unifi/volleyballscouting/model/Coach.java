package it.unifi.volleyballscouting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
public class Coach extends BaseModel implements UserDetails {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column
    private Date birthdate;
    private String phone;
    private String email;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    protected Coach (){}
    public Coach(String name, String surname, String username, Team team) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.team = team;
        setCreatedAt(LocalDateTime.now());
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getSurname() {return surname;}

    public void setSurname(String surname) {this.surname = surname;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_COACH"));
    }

    // FIX: prima restituiva "" (stringa vuota): con questo valore Spring Security
    // non poteva MAI autenticare un allenatore. Ora restituisce la password reale.
    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public Date getBirthdate() {return birthdate;}

    public void setBirthdate(Date birthdate) {this.birthdate = birthdate;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public Team getTeam() {return team;}

    public void setTeam(Team team) {this.team = team;}

    //----FOR SECURITY-----

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }
}
