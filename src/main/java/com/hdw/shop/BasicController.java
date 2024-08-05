package com.hdw.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Controller
public class BasicController {
    @GetMapping("/")
    @ResponseBody   // return에있는 값을 문자열 그대로보내주세요라는뜻 html파일보내려면 지워줘야함
    String hello() {
        return "index.html";
    }
    @GetMapping("/about")
    @ResponseBody
    String about(){
        return "피싱사이트에용";
    }
    @GetMapping("/mypage")
    @ResponseBody
    String mypage() {
        return "마이페이지";
    }

    @GetMapping("/date")
    @ResponseBody
    String date(){
        return LocalDateTime.now().toString();
    }



}
