package com.JynInfoConseil.users;

import com.JynInfoConseil.users.entities.Role;
import com.JynInfoConseil.users.entities.User;
import com.JynInfoConseil.users.sevice.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class UsersMicroserviceApplication {

	@Autowired
	UserService userService;
	public static void main(String[] args) {
		SpringApplication.run(UsersMicroserviceApplication.class, args);
	}

	@PostConstruct
		// init_users() sera executé juste après le constructeur de la class
	void init_users() {
//ajouter les rôles

		userService.addRole(new Role(null,"ADMIN"));
		userService.addRole(new Role(null,"USER"));
//ajouter les users
		userService.saveUser(new User(null,"admin","123",true,null));
		userService.saveUser(new User(null,"manal","456",true,null));
		userService.saveUser(new User(null,"safa","789",true,null));
//ajouter les rôles aux users
		userService.addRoleToUser("admin", "ADMIN");
		userService.addRoleToUser("admin", "USER");
		userService.addRoleToUser("manal", "USER");
		userService.addRoleToUser("safa", "USER");

	}
	@Bean
	//disponible tout au long du projet
	BCryptPasswordEncoder getBCE() {
		return new BCryptPasswordEncoder();
	}

}
