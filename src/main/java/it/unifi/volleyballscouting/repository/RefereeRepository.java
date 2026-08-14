package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.Referee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefereeRepository extends JpaRepository<Referee, Long> {

//only for findAll()
}