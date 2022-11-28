package com.cos.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private int id;

    @Column(nullable = false,length = 200)
    private String content;

    @ManyToOne // 여러개의 Reply는 , 하나의  게시글이 있다.  연관 관계를 심어주는 것
    @JoinColumn(name = "boardId")    //Board 테이블의 PK 사용하기
    private Board board;      //JPA가 알아서 다른 테이블을 JOIN해줌

    @ManyToOne
    @JoinColumn(name = "userId")  //User 테이블의 PK 사용하기
    private User user;       //JPA가 알아서 다른 테이블을 JOIN해줌

    @CreationTimestamp
    private Timestamp createDate;

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", board=" + board +
                ", user=" + user +
                ", createDate=" + createDate +
                '}';
    }

    /*BoardService에서 댓글쓰기() 메서드에서 Reply 생성자를 Builder 패턴으로 하는데 다른방법도 있는겁 보여주기!
    public void update(User user,Board board,String  content ){
        setUser(user);
        setBoard(board);
        setContent(content);
    }

    */
}
