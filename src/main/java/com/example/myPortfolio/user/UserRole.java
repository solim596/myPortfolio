package com.example.myPortfolio.user;

import lombok.Getter;

@Getter
public enum UserRole {
    // 상수값
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");
    
    // 생성자
    UserRole(String value) {
        this.value = value;
    }

    // 멤버변수
    private String value;
}
