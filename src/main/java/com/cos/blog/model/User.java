package com.cos.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

//ORM 은  Java(다른언어)  Object를 ->테이블로 매핑해주는 기술이다 .
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder   // 클래스에 @Builder 붙이면 모든 필드에 대한 빌더 메서드를 생성하고
           //  생성자에 @Builder 붙이면 생성자에 포함한 파라미터만 적용
@Entity  //User클래스가 자동으로 MySQL에 테이블이 생성이 된다 .
//@DynamicInsert  //insert 할 때 null 인 필드 제외 시켜줌 --별로 좋은 방식이 아님
public class User {

    @Id  //PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) //해당 프로젝트에서 연결된 DB의 넘버링 전략을 따라감
    private int id; //오라클 -시퀀스 , mysql-auto_increment

    @Column(nullable = false,length = 100,unique = true) //중복 제외
    private String username;//아이디

    @Column(nullable = false,length = 100) //패스워드를 해쉬로 암호화 하기위해 크게 잡음
    private String password;

    @Column(nullable = false,length = 50)
    private String email;

    //@ColumnDefault("'user'")  // "  '   '  "  임!!! --이거 쓰려면 DyanmicInsert 사용해야함
    //private String role;  // Enum을 쓰는게 좋다 .// admin , user , manager - String으로 바로주면 오타나거나 해서 별로
                          // enum을 사용해 도메인을 정하는게 좋다

    //DB는 RoleTYpe이라는게 없음
    @Enumerated(EnumType.STRING)
    private RoleType role; // Enum을 쓰기 위함 // ADMIN,USER


    @CreationTimestamp  //자바에서 시간이 자동 입력
    private Timestamp createDate;


    private String oauth; // kakao,google ..
}
