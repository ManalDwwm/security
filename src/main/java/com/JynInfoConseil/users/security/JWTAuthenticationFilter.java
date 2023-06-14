package com.JynInfoConseil.users.security;
import com.JynInfoConseil.users.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class JWTAuthenticationFilter extends
        UsernamePasswordAuthenticationFilter {
    //mon attribut de type AuthenticationManager + constructeur avec cet attribut
    private AuthenticationManager authenticationManager;
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager)
    {
        super();
        this.authenticationManager = authenticationManager;
    }
    @Override
    //cette methode sera appelé automatiquement par Spring sécurité lorsque nous avons une tentative d'authentification
    //cette méthode elle va extraire l'objet user à partir du request de La requête HTTP et désérialiser le format
    //Json en un objet de type User
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {

        User user =null;

        try {
    //user reçois new objectMapper() qui appartient à la biblio Json il sert à sériéliser et désérialiser
    //les objet vers et à partir de Json
    // il va lire ma requête HTTP et il va en extraire le User Sous format Jason
    // et il va le transformer en objet de type User la class
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //je retourne un objet de type authentificationManager mon attribut et j'appel ma méthode authenticate
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
    }
    @Override
    //pour savoir ce qu'il faut faire lorsque l'authentification réussit
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
                                   throws IOException, ServletException
    {
        //1) récupérer le user dans l'objet qui s'appelle springUser
        org.springframework.security.core.userdetails.User springUser =
                (org.springframework.security.core.userdetails.User)
                        authResult.getPrincipal();
         //2) maintenant que j'ai user je construit les authorities(roles) sous forme de liste
        // 3) et puis je construit mon JWT

        // je définit ma liste role
        List<String> roles = new ArrayList<>();
        //parcourir les roles (authorities) qui se trouve dans springUser
        springUser.getAuthorities().forEach(au-> {// Pour chèque autorité “au” dans cette liste,
            // Je vais ajouter cette autorité à une liste role
            roles.add(au.getAuthority());// maintenant rôles  est rempli avec une liste de chaînes
            // de caractère Des rôles ( autorités) de mon springUser .
        });
        // construction des différent champs du Token
        String jwt = JWT.create().
                withSubject(springUser.getUsername()).//le subject c'est mon username
                withArrayClaim("roles", roles.toArray(new String[roles.size()])).//claim qui contient les differents role
                withExpiresAt(new Date(System.currentTimeMillis()+SecParams.EXP_TIME)).//aprèes cette date le token n'est plus valable
                sign(Algorithm.HMAC256(SecParams.SECRET));//je signe avec l'algorithme HMAC256 +clé secrete
        //la valeur de mon token sera dans Authorization dans Header
        response.addHeader("Authorization", jwt);
    }


}
