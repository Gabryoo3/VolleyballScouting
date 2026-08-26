package it.unifi.volleyballscouting.config;

import it.unifi.volleyballscouting.security.AppUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    public SecurityConfig(AppUserDetailsService uds){
        this.userDetailService = uds;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                // Console H2 (solo per sviluppo/test del database)
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/players/list").permitAll()
                // Elenchi pubblici delle squadre consultabili anche dall'ospite (UC-06)
                .requestMatchers(HttpMethod.GET, "/teams", "/teams/details/**").permitAll()
                //pages that need roles to be accessed
                // FIX: prima "players/create" era senza "/" iniziale e non veniva mai applicato
                .requestMatchers("/players/new", "/players/create").hasRole("COACH")
                .requestMatchers("/players/*/edit", "/players/*/update").hasRole("COACH")
                .requestMatchers("/teams/new", "/teams/create").hasRole("COACH")
                .requestMatchers("/teams/*/edit", "/teams/*/update").hasRole("COACH")
                .anyRequest().authenticated()
        ).formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
        ).logout(logout -> logout
                .logoutSuccessUrl("/").permitAll());

        // Necessario perché la console H2 viene servita dentro un <iframe>
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        // La console H2 non invia il token CSRF: la si esclude (solo per sviluppo)
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
