package com.cos.blog.service;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


//스프링이 컴포넌트 스캔을 통해서 Bean에 등록해줌 -> IOC를 해준다는 의미
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional
    public void 회원가입(User user){
        String rawPassword=user.getPassword(); //1234원 문
        String encPassword=encoder.encode(rawPassword); //해쉬
        user.setPassword(encPassword);
        user.setRole(RoleType.USER);
        userRepository.save(user);

    }

  /*    전통적인 로그인 방식(시큐리티적용 안됨)
   @Transactional(readOnly = true)  //Select 할 때 트랜잭션 시작, 서비스 종료시에 트랜잭션 종료( 정합성 유지됨)
    public User 로그인(User user){

        return userRepository.findByUsernameAndPassword(user.getUsername(),user.getPassword());

    }*/


}
