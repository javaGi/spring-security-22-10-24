package com.zoho.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Setter
@Getter
@Table(name = "sign_up_user")
public class SignUpUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;



    @Column(name = "password", nullable = false, length = 1000)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "role", nullable = false, length = 20)
    private String role;


}