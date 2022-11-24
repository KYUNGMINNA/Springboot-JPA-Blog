package com.cos.blog.service;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Transactional(readOnly = true)
    public User 회원찾기(String username){

        //orElseGet() = 회원을 찾았는데 없으면 ,빈 객체 혹은 임의로 만든 객체를 리턴하라느 ㄴ의미
        User user= userRepository.findByUsername(username).orElseGet(()->{
           return new User();
        });
        return user;
    }



    @Transactional
    public void 회원가입(User user){
        String rawPassword=user.getPassword(); //1234원 문
        String encPassword=encoder.encode(rawPassword); //해쉬
        user.setPassword(encPassword);
        user.setRole(RoleType.USER);
        userRepository.save(user);

    }

    @Transactional
    public void 회원수정(User user){ /// 여기서 파라미터 user는 외부로 부터 받은 user
        //수정시에는 영속성 컨텍스트 User 오브젝트를 영속화시키고 , 영소화된 User 오브젝트를 수정 --이게 가장 바람직
        //Select를 해서 User오브젝트를 DB로부터 가져오는 이유는 영속화 하기 위해서
        //영속화된 오브젝트를 변경하면 자동으로 DB에 update문을 날려줍니다
        User persistance=userRepository.findById(user.getId()).orElseThrow( //findById가 null 값일 수 있어 orElseThrow 사용
                ()->{ return new IllegalArgumentException("회원 찾기 실패");
                });

        //Validate 체크 ! => oauth 필드에 값이 없으면 수정 가능
        //oauth를 체크하여 카카오 로그인 한 사람은 비밀번호,이메일 수정하지 못하게 막음
        //왜냐면 카카오 로그인한 사람이 비밀번호 바꾸고 난디 , 로그아웃했다가 카카오 로그인하면
        //비밀번호가 바뀐상태라 로그인이 막힘
        if(persistance.getOauth() ==null || persistance.getOauth().equals("")){
            //위의 persistance가 정상적으로 돌아오면 밑에거 수행
            String rawPassword=user.getPassword();
            String encPassword=encoder.encode(rawPassword);
            persistance.setPassword(encPassword);
            persistance.setEmail(user.getEmail());
        }
        //회원 수정 함수 죵료시==는 서비스 종료시 == 트랜잭션이 종료 == commit이 자동으로 수행됩니다!
        //commit이 자동화 된다는 의미는 =
        // 영속화된 persistance 객체의 변화가 감지되면(==더티체킹) 되어 변화된 것들을 update문을 날려줍니다.

    }

  /*    전통적인 로그인 방식(시큐리티적용 안됨)
   @Transactional(readOnly = true)  //Select 할 때 트랜잭션 시작, 서비스 종료시에 트랜잭션 종료( 정합성 유지됨)
    public User 로그인(User user){

        return userRepository.findByUsernameAndPassword(user.getUsername(),user.getPassword());

    }*/


}
