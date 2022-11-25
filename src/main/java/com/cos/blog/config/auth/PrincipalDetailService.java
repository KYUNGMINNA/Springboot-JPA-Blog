package com.cos.blog.config.auth;


import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service //Bean 등록
public class PrincipalDetailService  implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    //스프링이 로그인 요청을 가로챌때 , useranem ,password 변수 2개를 가로채는데
    //password 부분 처리는 알아서 함
    //username이 DB에 있는지만 확인해주면 됨.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User principal=userRepository.findByUsername(username)
                .orElseThrow(()->{
            return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."+username);
        });

        return new PrincipalDetail(principal);  //시큐리티의 세션에 유저 정보가 저장 됨
        // PrincipalDetail에서 생성자를 만들어 둬야 함
        // 여기 메서드의 리턴타입의 매개변수로 전달하지 않으면 아이디가 :user ,PW:콘솔창 번호 가 넘어가게됨
        //즉 내가 로그인한 아이디와 ,비밀번호가 넘어가는것이 아님!!
    }
}
