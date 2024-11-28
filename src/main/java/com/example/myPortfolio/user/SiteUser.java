package com.example.myPortfolio.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // unique : 유일한 값이어야 함. 중복 배제
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
}
