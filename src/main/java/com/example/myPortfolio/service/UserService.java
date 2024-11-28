package com.example.myPortfolio.service;

import com.example.myPortfolio.repository.UserRepository;
import com.example.myPortfolio.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // public : 외부에서 접근가능
    // SiteUser : 반환되는 데이터의 자료형
    public SiteUser create(String username, String password, String email) {
        // SiteUser클래스를 사용하여 user인스턴스 생성
        SiteUser user = new SiteUser();
        // user인스턴스에 사용자 아이디 세팅
        user.setUsername(username);
        // user인스턴스에 비밀번호 세팅
        user.setPassword(passwordEncoder.encode(password));
        // user인스턴스에 이메일 세팅
        user.setEmail(email);
        // userRepository의 save메서드 호출해거 user의 데이터를 DB의 site_user테이블에 저장
        this.userRepository.save(user);
        // create메서드를 호출하는 곳으로 user데이터 반환
        return user;
    }

    // 사용자 아이디를 인식하여 로그인했는지 안했는지 검사하기 위한 메서드
    public SiteUser getUser(String username) {
        // userRepository의 findByUsername메서드를 호출하여 username값에 해당하는 정보를 site_user테이블에서 가져와서 siteUser인스턴스에 저장
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        // siteUser의 정보를 이 메서드 호출한 곳으로 반환
        return siteUser.get();
    }

    // 아이디 중복 검사
    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
