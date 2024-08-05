package com.hdw.shop.member;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies == null){
            filterChain.doFilter(request, response); //다음필터 실행해주세요
            return;
        }

        var jwtCookie = ""; //cookie에서 jwt인거 찾아서 밸류값담자
        for (int i = 0; i < cookies.length; i++){
            if (cookies[i].getName().equals("jwt")){
                jwtCookie = cookies[i].getValue();
            }
        }
        Claims claim;
        try{
            claim = JwtUtil.extractToken(jwtCookie);
        }catch (Exception e){
            filterChain.doFilter(request,response);
            return;
        }
        var arr = claim.get("authorities").toString().split(",");
       var authorities =  Arrays.stream(arr).map(a-> new SimpleGrantedAuthority(a)).toList();

        var customUser = new CustomUser(
                claim.get("username").toString(),
                "none",
                authorities
        );
        customUser.displayName = claim.get("displayName").toString();

        var authToken = new UsernamePasswordAuthenticationToken(
                claim.get("username").toString(),""
        );

        authToken.setDetails(new WebAuthenticationDetailsSource()
                .buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response); //다음필터로
    }
}
