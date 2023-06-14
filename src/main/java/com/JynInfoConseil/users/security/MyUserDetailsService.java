package com.JynInfoConseil.users.security;
import com.JynInfoConseil.users.entities.User;
import com.JynInfoConseil.users.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserService userService;
    @Override
    //invoqué automatiquement par Spring Security lors d'une tentative de D'authentification
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        //je vais rempli mon user  grâce à la méthode findUserByUsername
        User user = userService.findUserByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Utilisateur introuvable !");//et je quitte la méthode

        //else
        //Je déclare une liste d'autorité : je vais la remplir avec les rôles de cet utilisateur user
        // trouvé dans la base de données
        List<GrantedAuthority> auths = new ArrayList<>();
        //récupérer roles de user car EAGER dans class User, les roles c'est une list
        //je vais parcourir ma list et pour chaque rôle de cette liste je vais lui faire un traitement
        user.getRoles().forEach(role -> {
         //je crée un objet  autority nouveau objet de la class SimpleGrantedAuthority et je lui donne comme argument role
            GrantedAuthority auhority = new SimpleGrantedAuthority(role.getRole());
          //je rajoute ce rôle à ma liste auths
            auths.add(auhority);
        });
        //j'appelle le constructeur de la class User de spring et je lui passe les 3 parametres
        // pour qu'il me retourne un objet de type userdetails

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), auths);
    }
}
