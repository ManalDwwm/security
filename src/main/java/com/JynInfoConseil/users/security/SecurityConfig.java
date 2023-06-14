package com.JynInfoConseil.users.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    // 1) authMgr pour retourner un JWT valide et permettre à l'utilisateur de se connecter
    //2) Bean créer en dessous

    AuthenticationManager authMgr;
    @Bean
    //l'objet de cette Bean sera injecté dans ma variable authMgr
    //authManager() retourne un objet de type AuthenticationManager qui va permettre à l'utilisateur de se connecter
    public AuthenticationManager authManager(
            HttpSecurity http,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            //userDetailsService Cet objet va contenir le login et le mot de passe de l'utilisateur qui veut s'authentifié
            // et la class MyUserDetailsService implémente cette interface UserDetailsService
            UserDetailsService userDetailsService)
            throws Exception {
                return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }//dès que cet objet est créee il sera injecté dans authMgr
    @Bean
    //3) utilisé le HttpSecurity pour dire à Spring les url autorisés
    public SecurityFilterChain filterChain(HttpSecurity http) throws
            Exception {
        http.csrf().disable();// je désactive csrf car je travail avec le JWT
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//à chaque appelle je passe mon
        // JWT dons STATELESSE pas besoin d'enregister les sessions
        http.authorizeRequests().requestMatchers("/login").permitAll();// indique les requettes (url) qui ont le
        // droit d'accéder à mon site. par url /login je reçois mon Token en fournissant request
        // dans body avec username et password valide
        http.authorizeRequests().requestMatchers("/all").hasAuthority("ADMIN");
        http.authorizeRequests().anyRequest().authenticated();// pour tout les autres url je demande authentification
        http.addFilter(new JWTAuthenticationFilter (authMgr)) ;
        //http.addFilterBefore(new JWTAuthenticationFilter (authMgr), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
