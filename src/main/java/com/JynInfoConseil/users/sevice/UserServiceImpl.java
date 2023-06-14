package com.JynInfoConseil.users.sevice;
import com.JynInfoConseil.users.entities.Role;
import com.JynInfoConseil.users.entities.User;
import com.JynInfoConseil.users.repos.RoleRepository;
import com.JynInfoConseil.users.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    //mot de passe crypté avec l'algorithme bCrypt avant d'être enregistré dans la BDD
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }
//rattacher les deux entités
    @Override
    public User addRoleToUser(String username, String rolename) {
        User usr=userRepository.findByUsername(username);
        Role role=roleRepository.findByRole(rolename);

        usr.getRoles().add(role);
        //je travaille en transactionnel
        //userRepository.save(usr);

        return usr;
    }
}
