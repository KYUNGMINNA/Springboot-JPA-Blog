package com.cos.blog.repository;

import com.cos.blog.model.Reply;
import com.cos.blog.model.ReplySaveRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ReplyReposiotry extends JpaRepository<Reply,Integer> {

    @Modifying
    @Query(value = "INSERT INTO reply(userId,boardId,content,createDate) VALUES(?1,?2,?3,now())",nativeQuery = true)
    int mSave(int userId,int boardId,String content); //dto의 변수들이 순서대로 ?에 들어감
    //이렇게 미리 만들어두면 영속화하지 않아도 된다.
    //업데이트 된 행의 개수를 리턴해 줌

}
