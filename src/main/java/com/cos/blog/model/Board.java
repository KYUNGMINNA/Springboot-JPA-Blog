package com.cos.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder   // 클래스에 @Builder 붙이면 모든 필드에 대한 빌더 메서드를 생성하고
            ///  생성자에 @Builder 붙이면 생성자에 포함한 파라미터만 적용
@Entity //Entity는 맨 밑에가 좋다
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private int id;

    @Column(nullable = false,length = 100)
    private String title;

    @Lob  //대용량 데이터 쓸 때 사용
    private String content;//섬머노트 라이브러리 <html>태그가 섞여서 디자인 됨

    //@ColumnDefault("0")
    private int count;//조회수

                //ManyToOne(fetch=FetchType.EAGER) 가 default이다
    @ManyToOne  //Many=Board   , User=One  = 한명의 유저는 여러개의 게시글 작성 가능
    @JoinColumn(name = "userId")               //FK가 된다
    private User user; // DB는 오브젝트를 저장할 수 없다.   자바는 오브젝트를 저장할 수 있다.
    //ORM은 오브젝트를 사용할 수 있다
    //FK로 만들어 짐!!!!!!!!!!!!!!!!!!!!

    @CreationTimestamp
    private Timestamp createDate;



    //OneToMany(fetch=FetchType.LAZY) 가 default 이다
    //mappedBy 는 Reply클래스의 private Board board; 에서 board를 가리킴!!
    //@JoinColumn(name="replyId") 필요없다>>이렇게 되면 여러개 댓글시 DB의 원자성(하나의 컬럼에는 하나값) 이깨짐
    @OneToMany(mappedBy = "board",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)  //mappedBy는 연관관계의 주인이 아니다(난 FK가 아니에요)DB에 컬럼을만들지마세요
    @JsonIgnoreProperties({"board","user"})  //무한 참조 방지 위한 어노테이션 --direct 접근은 보여주지만
                                            // 참조 형식으로 접근시 무한참조 방지
    @OrderBy("id desc ")
    private List<Reply> replys;     //단순히 JOIN만 해오겠다는 의미

    //@OneToMany(mappedBy = "board",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    //board 게시글을 삭제할 때 Replys 글 들도 모두 날리겠다



}




