package com.cos.blog.repository;

import com.cos.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

//DAO라고 치면 됨                                  //User테이블 , 기본키
//자동으로 Bean 등록이 된다
//@Repository  생략 가능
public interface UserRepository  extends JpaRepository<User, Integer> {

    //SELECT * FROM user WHERE username=1?;
    Optional<User> findByUsername(String username);




    /*
    전통적인 로그인 방식(시큐리티적용 안됨)

    //JPA Naming 쿼리
    // SELECT * FROM user WHERE username=?1 AND password=?2  이런 쿼리로 자동으로 생성되어 동작함
    //User findByUsernameAndPassword(String username,String password);

    // 위 아래 같은 코드로 동작 한다

    //@Query(value = "SELECT * FROM user WHERE username=? AND password=?",nativeQuery = true)


    //User login(String usname,String password);

    */

}
