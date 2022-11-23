package com.cos.blog.test;


import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyControllerTest {

    @Autowired  //의존성 주입
    private UserRepository userRepository;




    //htpp의 body에 useranme,password,email데이터 가지고 요청 하면
    @PostMapping("/dummy/join")
    public String join(@RequestParam("username") String u, String password, String email){ //key=value 규칙
                        //@RequestParam 생략할 수 있다.
        System.out.println("username:"+u);
        System.out.println("password:"+password);
        System.out.println("email:"+email);
        return "회원가입이 완료되었습니다.";
    }

    @PostMapping("/dummy/join2")
    public String join2(User user){
        System.out.println("id:"+user.getId());
        System.out.println("username:"+user.getUsername());
        System.out.println("password:"+user.getPassword());
        System.out.println("email:"+user.getEmail());
        System.out.println("role"+user.getRole());

        userRepository.save(user);

        return "회원가입이 완료되었습니다.";
    }


}
