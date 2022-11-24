package com.cos.blog.controller;


import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.util.UUID;

//Controller- 템플릿 용
//인증이 안된 사용자드링 추립할 수 있는 경로를 /auth/**허용
//그냥 주소가 / 이면 index.jsp 허용
//static 이하에 있는 /js/**  ,/css/**,/image/**
@Controller

public class UserController {


    @Value("${cos.key}")  // yml에 있는 cos : key 의 값 을 불러오는 것
    private String cosKey;

    @Autowired
    private UserService userService;


    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping("/auth/joinForm")
    public String joinForm() {

        return "user/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String loginForm() {

        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        //@AuthenticationPrincipal PrincipalDetail principal 은
        //SecurityContextHolder 내부의 SecurityContext 내부의 AUtehtication 을 가져옴
        return "user/updateForm";
    }


    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code) { //@ResponseBody 는 Data를 리턴해주는 컨트롤러 함수

        //POST방식으로 key=value 데이터를 요청(카카오 쪽으로 )
        //Retrofit2
        //OkHttp
        //RestTemplate


        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        //하드 코딩 방식이라 좋은 방법 아님
        params.add("grant_type", "authorization_code");
        params.add("client_id", "");
        params.add("redirect_uri", "http://localhost:8383/auth/kakao/callback");
        params.add("code", code);

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        //Http요청하rl -post 방식 -그리고 response변수 응답받음
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        //Gson,Json Simnple , ObjectMapper ...  JSON 객체를 자바 Object로 처리하기 위해 변환하는 과정
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;

        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("카카오 엑세스 토큰 " + oauthToken.getAccess_token());

        //return response.getBody(); 이거 쓸때는 메서드의 리턴타입 앞에 @ResponseBody


        RestTemplate rt2 = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
                new HttpEntity<>(headers2);

        //Http요청하rl -post 방식 -그리고 response변수 응답받음
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );

        System.out.println(response2.getBody());

        //return response2.getBody();   이거 쓸때는 메서드의 리턴타입 앞에 @ResponseBody


        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;

        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //User 오브젝트 : useranem,password,email
        System.out.println("카카오 아이디(번호)" + kakaoProfile.getId());
        System.out.println("카카오 이메일:" + kakaoProfile.getKakao_account().getEmail());

        System.out.println("블로그 서버  유저네임:"+kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
        System.out.println("블로그 서버 이메일:"+kakaoProfile.getKakao_account().getEmail());
        //UUID란 중복되지 않는 어떤 특정 값을 만들어내는 알고리즘
        System.out.println("쁠로그 서버 패스워드 :"+cosKey);

        User kakaoUser=User.builder()
                        .username(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId())
                        .password(cosKey)
                        .oauth("kakao") //카카오 로그인 한 사람들 비밀번호 변경 막기 위해
                        .email(kakaoProfile.getKakao_account().getEmail()).build();
        //가입자 혹은 비가입자 체크 해서 처리
        User originUser=userService.회원찾기(kakaoUser.getUsername());

        if(originUser.getUsername()==null){
            System.out.println("기존 회원이 아닙니다................!");
            userService.회원가입(kakaoUser);
        }

        System.out.println("자동 로그인을 진행합니다.");
        //로그인 처리
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(),cosKey));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";
    }
}
