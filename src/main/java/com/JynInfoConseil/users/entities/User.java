package com.JynInfoConseil.users.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@CrossOrigin("*")
@Table(name = "_user")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long user_id;

    @Column(unique=true)
    private String username;
    private String password;

    //true = user active
    private Boolean active;

    //EAGER pour ramener les roles en meme temps que le user
    @ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    //créer table user_role avec deux colonnes, avec id user et id role
    @JoinTable(name="user_role",joinColumns = @JoinColumn(name="user_id") ,
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private List<Role> roles;


}