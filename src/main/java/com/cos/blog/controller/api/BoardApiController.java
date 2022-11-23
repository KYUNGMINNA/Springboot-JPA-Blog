package com.cos.blog.controller.api;

import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserApiController {

    @Autowired
    private UserService userService;



    @PostMapping("/auth/joinProc")
    public ResponseDto<Integer> save(@RequestBody User user){

        System.out.println("UserApiController :Save 호출됨 ");
        //실제로 DB에 insert를 하고 아래에서 return 되면 되요


        userService.회원가입(user);
        //return new ResponseDto<Integer>(HttpStatus.OK,1);  //Dto에서 HttpStatus status 일때 value생략
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }

    /*  --전통적인 로그인 방식 ( 시큐리티 적용 안됨)
    @PostMapping("/api/user/login")       //세션을 매개변수에 적는방법과 =@Autowired로 사용하는 방법 이 있다
    public ResponseDto<Integer> login(@RequestBody User user,HttpSession session){
        System.out.println("UserApiController :login 호출됨 ");
        User principal=userService.로그인(user); //principal =접근 주체

        if(principal!=null){
            session.setAttribute("principal",principal);
        }
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }
    */
}
