package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.RefereeFormDTO;
import it.unifi.volleyballscouting.model.Referee;
import it.unifi.volleyballscouting.repository.RefereeRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
@Transactional (readOnly = true)
@Service
public class RefereeService {
    private final RefereeRepository refereeRepo;
    private final UserAccountService userAccountService;
    private final PasswordEncoder passwordEncoder;

    public RefereeService(RefereeRepository repo, UserAccountService userAccountService, PasswordEncoder passwordEncoder){
        this.refereeRepo = repo;
        this.userAccountService = userAccountService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Referee> findAll() { return refereeRepo.findAll();}

    public List<Referee> findBySurname(String surname) { return refereeRepo.findBySurname(surname);}

    public Referee findById(UUID id){
        return refereeRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Arbitro specificato non trovato"));
    }

    @Transactional
    public String save(RefereeFormDTO form){
        if(userAccountService.isUsernameTaken(form.username()))
            throw new IllegalArgumentException("Lo username inserito è già in uso");
        String tempPassword = UUID.randomUUID().toString().substring(0,8);
        String encodedPassword = passwordEncoder.encode(tempPassword);

        Referee r = new Referee(form.name(), form.surname(), form.username(), encodedPassword, form.birthdate());
        r.setBirthdate(form.birthdate());
        r.setPhone(form.phone());
        r.setEmail(form.email());
        refereeRepo.save(r);
        return tempPassword;
    }

    @Transactional
    public void updateReferee(UUID refereeId, RefereeFormDTO form){
        Referee r = findById(refereeId);
        r.updateFromDto(form);
    }
}
