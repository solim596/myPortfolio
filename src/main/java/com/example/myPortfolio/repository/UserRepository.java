package com.example.myPortfolio.repository;

import com.example.myPortfolio.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    // 인터페이스에서는 메서드를 선언만 하고, service에서 호출해서 사용함
    Optional<SiteUser> findByUsername(String username);
    // site_user테이블에 사용자가 입력한 아이디가 존재하는지 검사하는 메서드
    boolean existsByUsername(String username);
}
