package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.Address;
import it.unifi.volleyballscouting.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    // Esempio: trova una squadra tramite il nome
}