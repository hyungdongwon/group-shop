package com.hdw.shop.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor //DB문법 입출력할때 사용
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void join(Member member) throws Exception{
        Optional <Member> result = memberRepository.findByUsername(member.getUsername());
        if(result.isPresent()){
            throw new Exception("존재하는 아이디");
        }
        if(member.getUsername().length() < 8 || member.getPassword().length() < 8 ){
            throw new Exception("아이디 또는 비밀번호가 너무짧습니다");
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);

    }


}
