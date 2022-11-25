package com.cos.blog.config.auth;

import com.cos.blog.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면  UserDetails 타입의 오브젝트를
// 스프링 시큐리티의 고유한 세션저장소에 저장을 해준다.
@Getter //User user 를 꺼내기 위함
public class PrincipalDetail implements UserDetails {  //UserDetails는 시큐리티에서 제공함
    private User user;  //콤포지션==클래스 내부에 객체를 가지고 있는 것


    public PrincipalDetail(User user) {
        this.user=user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정이 만료되지 않았는지 리턴한다(true : 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨있지 않았는지 리턴한다(true : 잠기지않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호가 만료되지 않았는지 리턴한다 (true :만료 안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정 활성화(사요가능)인지 리턴한다 (true :활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    //계정이 갖고 있는 권한 목록을 리턴한다.(권한이 여러개 있을 수 있어서 루프를 돌아야 하는데 우리는 한개만)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       Collection<GrantedAuthority> collectors=new ArrayList<>();

       /*
       collectors.add(new GrantedAuthority() {
           @Override  // "ROLE_" 를 꼭 붙여야 함 ! 규칙임!!
           public String getAuthority() {
               return "ROLE_"+user.getRole();  //ROLE_USER
           }
       });
        */
        //위의  코드 5줄을 람다식으로 표현
       collectors.add(()->{ return "ROLE_"+user.getRole();});

        return collectors;
    }

}
