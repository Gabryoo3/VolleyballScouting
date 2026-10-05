package it.unifi.volleyballscouting.security;

import it.unifi.volleyballscouting.dto.UserAccount;
import it.unifi.volleyballscouting.model.Admin;
import it.unifi.volleyballscouting.model.Coach;
import it.unifi.volleyballscouting.model.Player;
import it.unifi.volleyballscouting.repository.AdminRepository;
import it.unifi.volleyballscouting.repository.CoachRepository;
import it.unifi.volleyballscouting.repository.PlayerRepository;
import it.unifi.volleyballscouting.service.UserAccountService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final CoachRepository coachRepository;
    private final PlayerRepository playerRepository;
    private final AdminRepository adminRepository;
    private final UserAccountService userAccountService;

    public AppUserDetailsService(CoachRepository cr, PlayerRepository pr, AdminRepository ar, UserAccountService userAccountService){
        this.coachRepository = cr;
        this.playerRepository = pr;
        this.adminRepository = ar;
        this.userAccountService = userAccountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccount> accountOpt = userAccountService.findByUsername(username);
        if(accountOpt.isEmpty()){
            throw new UsernameNotFoundException("Utente non trovato con username: " + username);
        }
        UserAccount account = accountOpt.get();
        return new AppUserDetails(
            account.id(),
            account.username(),
            account.password(),
            account.role()
        );
    }
}
