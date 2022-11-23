package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Application.yml 파일에 jsp 위치로 셋팅해놔서 이 메ㅓㅅ드 동작 안함 .
@Controller //RestController와 달리 기본적으로 파일경로를 리턴해준다.
public class TempControllerTest {

    //http:localhost:8383/blog/temp/home
    @GetMapping("/temp/home")
    public String tempHome(){
        //스프링의 파일리턴 기본경로 : src/main/resources/static
        //리턴명 :/home.html

        //풀경로 : src/main/resources/static/home.html
        return "/home.html";
    }

    @GetMapping("/temp/jsp")
    public String tempJsp(){

        return "test";
    }
}
