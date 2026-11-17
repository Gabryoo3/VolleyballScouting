package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.UserAccount;
import it.unifi.volleyballscouting.exception.BusinessRuleException;
import it.unifi.volleyballscouting.exception.ResourceNotFoundException;
import it.unifi.volleyballscouting.model.*;
import it.unifi.volleyballscouting.repository.AdminRepository;
import it.unifi.volleyballscouting.repository.CoachRepository;
import it.unifi.volleyballscouting.repository.PlayerRepository;
import it.unifi.volleyballscouting.repository.RefereeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional(readOnly = true)
@Service
public class UserAccountService {

    private final AdminRepository adminRepository;
    private final CoachRepository coachRepository;
    private final PlayerRepository playerRepository;
    private final RefereeRepository refereeRepository;
	private final PasswordEncoder passwordEncoder;

	public UserAccountService(AdminRepository adminRepository, CoachRepository coachRepository, PlayerRepository playerRepository, RefereeRepository refereeRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.coachRepository = coachRepository;
        this.playerRepository = playerRepository;
        this.refereeRepository = refereeRepository;
		this.passwordEncoder = passwordEncoder;
	}

    public Optional<UserAccount> findByUsername(String username){
        if (username == null || username.isBlank()) return Optional.empty();

        Optional<Admin> adminOpt = adminRepository.findByUsernameIgnoreCase(username);
        if(adminOpt.isPresent()){
            Admin a = adminOpt.get();
            return Optional.of(
                    new UserAccount(
                    a.getId(),
                    a.getUsername(),
                    a.getPassword(),
                    "ADMIN"
                    )
            );
        }
        Optional<Coach> coachOpt = coachRepository.findByUsernameIgnoreCase(username);
        if (coachOpt.isPresent()){
            Coach c = coachOpt.get();
            return Optional.of(
                    new UserAccount(
                            c.getId(),
                            c.getUsername(),
                            c.getPassword(),
                            "COACH"
                    )
            );
        }
        Optional<Referee> refereeOpt = refereeRepository.findByUsernameIgnoreCase(username);
        if(refereeOpt.isPresent()){
            Referee r = refereeOpt.get();
            return Optional.of(
                    new UserAccount(
                            r.getId(),
                            r.getUsername(),
                            r.getPassword(),
                            "REFEREE"
                    )
            );
        }
        Optional<Player> playerOpt = playerRepository.findByUsernameIgnoreCase(username);
        if (playerOpt.isPresent()){
            Player p = playerOpt.get();
            return Optional.of(
                    new UserAccount(
                            p.getId(),
                            p.getUsername(),
                            p.getPassword(),
                            "PLAYER"
                    )
            );
        }
        return Optional.empty();
    }

	public Account findAccountById(UUID id) {
		return adminRepository.findById(id).map(Account.class::cast)
				.or(() -> coachRepository.findById(id).map(Account.class::cast))
				.or(() -> refereeRepository.findById(id).map(Account.class::cast))
				.or(() -> playerRepository.findById(id).map(Account.class::cast))
				.orElseThrow(() -> new ResourceNotFoundException("Account non trovato"));
	}

    public boolean isUsernameTaken(String username){
        return findByUsername(username).isPresent();
    }

	@Transactional
	public void changePassword(UUID accountId, String currentPassword, String newPassword, String repeatPassword){
		Account user = findAccountById(accountId);
		if (!newPassword.equals(repeatPassword))
			throw new BusinessRuleException("La nuova password e la ripetuta non coincidono");
		if (!passwordEncoder.matches(currentPassword, user.getPassword()))
			throw new BusinessRuleException("La password attuale non è corretta");
		if(!passwordEncoder.matches(newPassword, user.getPassword()))
			throw new BusinessRuleException("La nuova password non può essere uguale alla vecchia");
		String encoded = passwordEncoder.encode(newPassword);
		user.setPassword(encoded);
	}

}



