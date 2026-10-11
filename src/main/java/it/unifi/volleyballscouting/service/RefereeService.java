package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.model.Referee;
import it.unifi.volleyballscouting.repository.RefereeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class RefereeService {
    private final RefereeRepository refereeRepo;

    public RefereeService(RefereeRepository repo){
        this.refereeRepo = repo;
    }

    public List<Referee> findAll() { return refereeRepo.findAll();}

    public List<Referee> findBySurname(String surname) { return refereeRepo.findBySurname(surname)};

    public Referee findById(UUID id){
        return refereeRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Arbirtro specificato non trovato"));
    }

    @Transactional
    public Referee save(RefereeFormDTO form){
        //TODO: DTO FORM FOR REFEREE AND SAVE

    }
}
