package com.hdw.shop;

import com.hdw.shop.member.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    @Bean
    @Transactional
    PasswordEncoder PasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /*http.csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository())
                .ignoringRequestMatchers("/login")
        );*/
        http.csrf((csrf)-> csrf.disable());

        http.addFilterBefore(new JwtFilter(), ExceptionTranslationFilter.class);

        http.sessionManagement((session)-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests((authorize) ->
                authorize.requestMatchers("/**").permitAll()
        );
       /* http.formLogin((formLogin) //폼으로 로그인하겠습니다
                -> formLogin.loginPage("/login") //로그인페이지
                .defaultSuccessUrl("/my-page") //로그인 성공시 이동시킬 url
                *//*.failureUrl("/fail") //실패했을때 보낼 url*//* //이걸지정해주지않으면 주소창뒤에 ?error 이라는게생김
        );*/
        http.logout(logout
                -> logout.logoutUrl("/logout") //로그아웃
                .logoutSuccessUrl("/list")
                .invalidateHttpSession(true)
        );
        return http.build();
    }
}
