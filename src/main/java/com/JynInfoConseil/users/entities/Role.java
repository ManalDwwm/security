package com.JynInfoConseil.users.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;



@Data @NoArgsConstructor
@AllArgsConstructor
@Entity
@CrossOrigin("*")
public class Role {
    @Id
    @GeneratedValue (strategy=GenerationType.IDENTITY)
    private Long role_id;
    private String role;

}
