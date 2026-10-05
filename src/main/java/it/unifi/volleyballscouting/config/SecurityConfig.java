package it.unifi.volleyballscouting.config;

import it.unifi.volleyballscouting.model.Admin;
import it.unifi.volleyballscouting.security.AppUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AppUserDetailsService userDetailService;

    public SecurityConfig(AppUserDetailsService uds) {
        this.userDetailService = uds;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                // Risorse statiche e pagine pubbliche di base
                .requestMatchers("/", "/login", "/home", "/css/**", "/js/**", "/images/**").permitAll()

                // Console H2 (solo per sviluppo locale, ripristinato a permitAll per comodità di test)
                .requestMatchers("/h2-console/**").hasRole("ADMIN")

                // ==========================================
                // 1. CONSULTAZIONE PUBBLICA CAMPIONATO (Ospite / Giocatore)
                // ==========================================
                .requestMatchers(HttpMethod.GET, "/teams", "/teams/list", "/teams/details/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/players/list", "/players/details/**", "/players/free").permitAll()
                .requestMatchers(HttpMethod.GET, "/matches", "/matches/list", "/matches/details/**").permitAll()

                // ==========================================
                // 2. AMMINISTRATORE (Gestione Campionato e Referti)
                // ==========================================
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/matches/new", "/matches/create").hasRole("ADMIN")
                .requestMatchers("/matches/*/edit", "/matches/*/update", "/matches/*/score/**", "/matches/*/sets/**").hasRole("ADMIN")

                // ==========================================
                // 3. ALLENATORE (Rosa Giocatori e Live Scouting)
                // ==========================================
                .requestMatchers("/players/new", "/players/create").hasRole("COACH")
                .requestMatchers("/players/*/edit", "/players/*/update").hasRole("COACH")
                .requestMatchers("/teams/new", "/teams/create").hasRole("COACH")
                .requestMatchers("/teams/*/edit", "/teams/*/update").hasRole("COACH")
                .requestMatchers("/matches/*/scout/**").hasRole("COACH")

                // Qualsiasi altra richiesta richiede autenticazione
                .anyRequest().authenticated()
        ).formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
        ).logout(logout -> logout
                .logoutSuccessUrl("/").permitAll());

        // Configurazione per iframe della console H2
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}