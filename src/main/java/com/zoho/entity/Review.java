package com.zoho.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private int rating;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "sign_up_user_id", nullable = false)
    private SignUpUser signUpUser;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

}
