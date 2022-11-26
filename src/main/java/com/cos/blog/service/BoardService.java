package com.cos.blog.service;

import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.*;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyReposiotry;
import com.cos.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


//스프링이 컴포넌트 스캔을 통해서 Bean에 등록해줌 -> IOC를 해준다는 의미
@Service
//@RequiredArgsConstructor  // 생성자 만들 때 초기화가 필수인것들을 자동으로 초기화 하게끔
public class BoardService {
    //@RequiredArgsConstructor 쓰려면 final 을 써야 함
    //private final BoardRepository boardRepository;
    //private final ReplyReposiotry replyReposiotry;


    //생성자 의존성 주입
    private  BoardRepository boardRepository;
    private  ReplyReposiotry replyReposiotry;
   @Autowired
    public BoardService(BoardRepository boardRepository, ReplyReposiotry replyReposiotry) {
        this.boardRepository = boardRepository;
        this.replyReposiotry = replyReposiotry;
    }


    @Autowired
    private UserRepository userRepository;
    @Transactional
    public void 글쓰기(Board board,User user){ //title,content

        board.setCount(0);
        board.setUser(user);

        boardRepository.save(board);
    }
    @Transactional(readOnly = true)
    public Page<Board> 글목록(Pageable pageable){
        return boardRepository.findAll(pageable);
    }


    @Transactional(readOnly = true)
    public Board 글상세보기(int id){
        return boardRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("글 상세보기 실패 :아이디를 찾을 수 없습니다.");
        });
    }

    @Transactional
    public void 글삭제하기(int id){
        boardRepository.deleteById(id);
    }


    @Transactional
    public void 글수정하기(int id,Board requestBoard){
       Board board= boardRepository.findById(id).orElseThrow(
               ()->{
                   return new IllegalArgumentException("글 찾기 실패 :아이디를 찾을 수 없습니다.");
               });//영속화 완료
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
        //해당 함수로 종료시에(Service)가 종료될 때 트랜잭션이종료됩니다. 이때 더티체킹 -- 자동으로 업데이트 db flush

    }

    @Transactional
    //public void 댓글쓰기(User user, int boardId, Reply requestReply){  dto 사용 전
    public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto){


        /* dto 사용 전
         Board board=boardRepository.findById(boardId).orElseThrow(()->{
            return new IllegalArgumentException("댓글 쓰기 실패 : 게시글 id를 찾을 수 없습니다.");
        });;
        requestReply.setUser(user);
        requestReply.setBoard(board);
        replyReposiotry.save(requestReply);

        replyReposiotry.save(reply);
        */



        /* 영속화 하여 사용하는 방법 DTO를 만들어서 사용하는 방법으로 (보내거나 받을 데이터가 많을때는 DTO가 더 좋다)
        User user=userRepository.findById(replySaveRequestDto.getUserId()).orElseThrow(()->{
            return new IllegalArgumentException("댓글 쓰기 실패 : 유저 id를 찾을 수 없습니다.");
        });; //영속화 완료

        Board board=boardRepository.findById(replySaveRequestDto.getBoardId()).orElseThrow(()->{
            return new IllegalArgumentException("댓글 쓰기 실패 : 게시글 id를 찾을 수 없습니다.");
        });; //영속화 완료



        Reply reply=Reply.builder()
                .user(user)
                .board(board)
                .content(replySaveRequestDto.getContent())
                .build();

        //Reply 클래스에 함수를 만들어서 위의 Builder 패턴을  이용 안하는 방법도 있다.
        //Reply reply=new Reply();
        //eply.update(user,board,replySaveRequestDto.getContent());

        replyReposiotry.save(reply);
        */


        //ReplyRepository에 mSave() 메서드를 새롭게 제작하여 영속화 안하는 방법
        replyReposiotry.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());

    }

    @Transactional
    public void 댓글삭제(int replyId){
        replyReposiotry.deleteById(replyId);
    }



}
