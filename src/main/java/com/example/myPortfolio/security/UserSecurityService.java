package com.example.myPortfolio.security;

import com.example.myPortfolio.repository.UserRepository;
import com.example.myPortfolio.user.SiteUser;
import com.example.myPortfolio.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//UserDetailsService : 사용자의 정보를 담는 인터페이스
//implements : 인터페이스를 부모객체로 상속받을 때 사용하는 키워드
@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //null값을 포함하는 _siteUser인스턴스를 생성하고, userRepository의 findByUsername메서드를 호출하여 사용자 정보를 가져와서 _siteUser인스턴스에 저장(id, username, password, email)
        Optional<SiteUser> _siteUser = this.userRepository.findByUsername(username);
        //만약 _siteUser인스턴스가 비어있으면 메시지 출력
        if(_siteUser.isEmpty()){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        //SiteUser클래스로 siteUser인스턴스를 만들고 _siteUser의 정보를 얻어와서 siteUser인스턴스에 저장
        SiteUser siteUser = _siteUser.get();
        //GrantedAuthority : 사용자 아이디와 비밀번호를 UserDetailsService를 통해서 조회하는 객체이고, 어플리케이션 전반에 걸쳐 권한 관리함.
        List<GrantedAuthority> authorities = new ArrayList<>();
        //사용자 아이디가 admin이면 사용자 권한 (ROLE_ADMIN)을 주고
        if("admin".equals(username)){
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
            //사용자 아이디가 admin이 아니면 사용자 권한 (ROLE_USER)을 줌
        }else{
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        //스프링 부트에서 지원하는 User클래스로 새로운 인스턴스를 생성하고, 사용자아이디와 비밀번호, 권한 설정한 값을 인스턴스에 저장
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }

}
