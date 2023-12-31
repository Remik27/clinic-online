package pl.zajavka.infrastructure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "true", matchIfMissing = true)
    SecurityFilterChain securityEnabled(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/",
                                        "/registration",
                                        "/home",
                                        "/nfz/**",
                                        "/free-terms",
                                        "/select-specialization",
                                        "/book-term/{freeTermId}",
                                        "/registered-successfully",
                                        "/book-term",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/api/**").permitAll()
                                .requestMatchers("/patient-panel",
                                        "/visit-details/**",
                                        "/finished-visits/**",
                                        "/upcoming-visits/**",
                                        "/cancel-visit/**").hasAuthority(Roles.PATIENT.name())
                                .requestMatchers("/doctor-panel",
                                        "/add-free-terms-form",
                                        "/add-free-terms",
                                        "/doctor/upcoming-visits",
                                        "/patient-history/{visitId}",
                                        "/doctor/finished-visits",
                                        "/doctor/visit-details/{visitId}",
                                        "/add-description",
                                        "/add-diagnosis",
                                        "/finish-visit/{visitId}",
                                        "/added-successfully").hasAuthority(Roles.DOCTOR.name())

                )
                .formLogin(Customizer.withDefaults())
                .logout((logout) ->
                        logout.deleteCookies("JSESSIONID")
                                .invalidateHttpSession(false)
                                .logoutSuccessUrl("/")
                                .permitAll()
                );

        return http.build();
    }

    @Bean
    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "false")
    SecurityFilterChain securityDisabled(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests.anyRequest()
                                .permitAll());

        return http.build();
    }

}

