package com.hdw.shop.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @GetMapping("/join")
    String join(){
        return "join.html";
    }

    @PostMapping("/joinProc")
    ResponseEntity<String> joinProc(@ModelAttribute Member member) throws Exception {
       memberService.join(member);
        return ResponseEntity.status(200).body("회원가입 성공");
    }
    @GetMapping("/login")
    String login(Authentication auth){

        if(auth != null){
            return "redirect:/list";
        }
        return "login.html";
    }

    @GetMapping("/logout")
    String logout(){

        return "redirect:/list";
    }

    @PreAuthorize("isAuthenticated()") // 소괄호 안이 true 인경우에만 밑에코드 실행
    @GetMapping("/my-page")
    public String myPage(Authentication auth){ //로그인정보 보는것
        System.out.println("principal: "+auth);
        System.out.println("로그인한 ID: "+auth.getName()); //아이디출력
        System.out.println("로그인여부: "+ auth.isAuthenticated() );//로그인 여부 검사기능 
        System.out.println("권한: "+auth.getAuthorities().contains(new SimpleGrantedAuthority("일반유저")));

        CustomUser result = (CustomUser)auth.getPrincipal(); //로그인정보 회원이름을 빼기위해 메소드하나만듬
        System.out.println(result.displayName);
        return "mypage.html";
    }

    @PostMapping("/login/jwt")
    @ResponseBody
    public String loginJWT(@RequestBody Map<String,String> data, HttpServletResponse response){
        var authToken = new UsernamePasswordAuthenticationToken(
                data.get("username"),
                data.get("password")
        );
        var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        var jwt = JwtUtil.createToken( SecurityContextHolder.getContext().getAuthentication());
        System.out.println(jwt);

        var cookie = new Cookie("jwt",jwt);
        cookie.setMaxAge(100);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return jwt;
    }
    @GetMapping("/my-page/jwt")
    @ResponseBody
    String mypageJWT(Authentication auth){
        //jwt 위조가 없는지,유효기간은 안지났는지 확인하는 코드

        var user = (CustomUser) auth.getPrincipal();
        System.out.println(user);
        System.out.println(user.displayName);
        System.out.println(user.getAuthorities());

        return "마이페이지데이터";
    }

    class MemberDto {
        public String username;
        public String  displayName;
        public Long id;
        MemberDto(String a,String b,long c){
            this.username = a;
            this.displayName = b;
            this.id = c;
        }
    }


}
