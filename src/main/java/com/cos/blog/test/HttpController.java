package com.cos.blog.test;


//사용자가 요청 -> 응답(HTML파일)
//@Controller

import org.springframework.web.bind.annotation.*;

//사용자가 요청 -> 응답 (Data)
@RestController
public class HttpController{

    //인터넷 브라우저 요청은 get요청 밖에 할 수 없다.
    //http://localhost:8383/http/get
    @GetMapping("/http/get")
    public String getTest(Member m){ //MessageConverter가 쿼리스트링 값을 Object형태로 매핑시켜줌
        return "get 요청"+m.getId()+","+m.getUsername();
    }

    //post , put ,delete 요청을 테스트 해보기 위해 postman을 사용한다.
    @PostMapping("/http/post2")
    public String postTest(@RequestBody String text){ // raw + text
        return "post 요청"+text;
    }


    //post방식으로 + raw + JSON 형태로 전송할 때   -- JSON {  "key" :value} 형태 key는 문자열
    @PostMapping("/http/post")
    public String postTest(@RequestBody Member m){
        return "post 요청"+m.getId()+","+m.getUsername()+","+m.getPassword()+","+m.getEmail();
    //스프링 부트가 MessageConverter를 이용해 파싱하여 Object의 형태로 넣어줌(출력해줌)
    }
    @PutMapping("/http/put")
    public String putTest(@RequestBody Member m){
        return "put 요청"+m.getId()+","+m.getUsername()+","+m.getPassword()+","+m.getEmail();
    }

    @DeleteMapping("/http/delete")
    public String delteTest(){
        return "delete 요청";
    }
}
