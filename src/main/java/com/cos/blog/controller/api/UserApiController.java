package com.cos.blog.controller.api;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


//ApiController는 ajax용 - 앱이나 리액트 연동을 위해 주로 사용
@RestController
public class UserApiController {

    @Autowired
    private UserService userService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/auth/joinProc")
    public ResponseDto<Integer> save(@RequestBody User user){

        System.out.println("UserApiController :Save 호출됨 ");
        //실제로 DB에 insert를 하고 아래에서 return 되면 되요


        userService.회원가입(user);
        //return new ResponseDto<Integer>(HttpStatus.OK,1);  //Dto에서 HttpStatus status 일때 value생략
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
        //HttpStatus.OK.value() ==200 으로 리턴됨 !!
        //<Integer>라고해서 숫자 1 이 나옴
        //예외처리에서는 <String> 이라고 해서 문자열 넣음 !
    }

    @PutMapping("/user")  //@RequestBody가 없으면  JSON 타입의 데이터를 받을 수없고, x-www.form-urlencode 타입만 가능!!!
    public ResponseDto<Integer> update(@RequestBody User user){
                                    //key=value 형태 ,x-www.form-urlencode 로 받고 싶으면 @ReuqstBody 생략하세요
        userService.회원수정(user);

        //여기서 트래잭션이 종료되기 때문에 DB에 값은 변경이 됐음
        //하지만 세션값은 변경되지 않은 상태이기 때문에 우리가 직접 세션값을 변경해줄 것임
        //세션 등록 --스프링 시큐리티 authentication 객체 수정
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);


        //위의 방법과 동일함
        /*
        (@AuthenticationPrincipal PrincipalDetail principal,HttpSession session)
        Authentication authentication=new UsernamePasswordAuthenticationToken(principal,null);
        SecurityContext securityContext=SecurityContextHolder.getContext();
        securityContext.getAuthentication(authentication);
        session.setAttribute("SPRING_SECURITY_CONTEXT",securityContext);
        */


        // 강제로 스프링시큐리티 가 갖고있는 세션을 업데이트




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
