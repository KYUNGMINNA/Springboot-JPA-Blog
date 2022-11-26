package com.cos.blog.config;

import com.cos.blog.config.auth.PrincipalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//빈 등록 : 스프링 컨테이너에ㅓㅅ 객체를 관리 ㅏㄹ 수 있게 하는 것

//3개의 어노테티션은 스프링 시큐리티의 세트 -- 이해 안가면 그냥 같이 걸자 ~
@Configuration //빈 등록 ( IOC 로관리된다 )
@EnableWebSecurity //시큐리티 필터가 등록(추가)이 된다= 스프링 시큐리티가 활성화가 되어 있는데 ,어떤 설정을 해당 파일에서 진행한다.
@EnableGlobalMethodSecurity(prePostEnabled = true)  //특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다.
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    @Autowired
    private PrincipalDetailService principalDetailService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
    @Bean  //IoC가 되요
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }


    //시큐리티가 대신 로그인 해주는데 password를 가로채기는 하는데
    //해당 password가 뭘로 해시가 되어 회원강비이 되었는지 알아야
    //가은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음 .
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //antMatchers 를 제외한 모든 페이지는 인증이 되야 접속이 가능한 페이지

        http     //스프링 시큐리티는 요청시에 csrf 토큰을 가지고 있지 않으면 동작을 막음 그래서 disalbe해야함
                .csrf().disable() //csrf 토큰 비활성화 (테스트시 걸어두는게 좋음) -나중엔 없애야 함
                .authorizeHttpRequests()
                .antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**","/dummy/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/auth/loginForm")
                .loginProcessingUrl("/auth/loginProc") //스프링 시큐리티가 해당 주소로 요청이 오는 로그인 가로채서 대신로그인 해줌
                .defaultSuccessUrl("/");//정상적으로 요청이 완료되면 이동
        //.failureUrl("/fail")  -- 이거는 실패시 이동할 URL이다
        //  /auth/** 로 시작하는 모든 페이지는 인증이 필요하며 ,
        //   and , formLogin ,loginPage  로 인하여 인증안되어 있을시 로그이 페이지로 이동함

    }


    /*  --extends WebSecurityConfigurerAdapter deprecated되어 이렇게 사용하거나 해야함
    // 시큐리티는  x-www-form-urlencoded 타입데이터만 파싱

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http     //스프링 시큐리티는 요청시에 csrf 토큰을 가지고 있지 않으면 동작을 막음 그래서 disalbe해야함
                .csrf().disable() //csrf 토큰 비활성화 (테스트시 걸어두는게 좋음) -나중엔 없애야 함
                .authorizeHttpRequests()
                .antMatchers("/","/auth/**","/js/**","/css/**","/image/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/auth/loginForm")
                .loginProcessingUrl("/auth/loginProc") //스프링 시큐리티가 해당 주소로 요청이 오는 로그인 가로채서 대신로그인 해줌
                .defaultSuccessUrl("/");//정상적으로 요청이 완료되면 이동
                //.failureUrl("/fail")  -- 이거는 실패시 이동할 URL이다
                //  /auth/** 로 시작하는 모든 페이지는 인증이 필요하며 ,
                //   and , formLogin ,loginPage  로 인하여 인증안되어 있을시 로그이 페이지로 이동함

        return http.build();


    }
   */


}


