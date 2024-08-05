package com.hdw.shop.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
/*        DB에서 username을 가진 유저를 찾아와서
        return new User(유저아이디, 비번, 권한) 해주세요*/
        Optional<Member> result = memberRepository.findByUsername(username);
        System.out.println(result);
        if(result.isEmpty()){
            throw new UsernameNotFoundException("그런아이디 없음");
        }
        var user = result.get();
        List<GrantedAuthority> grant = new ArrayList<>(); //권한을 부여할땐LIST<GrantedAuthority> 에 담아서 부여함
        grant.add(new SimpleGrantedAuthority("일반유저"));  //일반메모임 나중에 빼서쓰면됨
        var a = new CustomUser(user.getUsername(),user.getPassword(),grant);
        a.displayName = user.getDisplayName();
        a.id = user.getId();
        return a;
    }
}

