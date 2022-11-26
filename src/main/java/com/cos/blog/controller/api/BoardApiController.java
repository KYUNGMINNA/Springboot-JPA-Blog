package com.cos.blog.controller.api;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.ReplySaveRequestDto;
import com.cos.blog.model.User;
import com.cos.blog.service.BoardService;
import com.cos.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


//ApiController는 ajax용 - 앱이나 리액트 연동을 위해 주로 사용
@RestController
public class BoardApiController {

    @Autowired
    private BoardService boardService;


    @PostMapping("/api/board")
    public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal){
        boardService.글쓰기(board,principal.getUser());
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }
    @DeleteMapping("/api/board/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable int id){
        boardService.글삭제하기(id);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }

    @PutMapping("/api/board/{id}")
    public ResponseDto<Integer> update(@PathVariable int id ,@RequestBody Board board){
        boardService.글수정하기(id,board);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
        /*
        메타코딩
        11개월 전
        정석은 ResponseEntity를 리턴하고
        서비스에서 터지는건 글로벌 익섹션 처리해주고 DTO는 제네릭 감싸서 상태코드 메시지 제네릭 데이터로 해주면 되요.

        api컨트롤러에서 그냥 서비스로직만 해놓고 redirect해서 다른 html페이지로 이동하는게 보기 깔끔하지 않나요?
        --상태코드도 보낼수있고 메시지도 보낼수 있다눈걸 보여주고 싶었던거
         */


    }

    //데이터를 받을 때 컨트롤러에서 dto를 만들어서 받는게 좋다 .
    //dto를 사용하지 않은 이유는
    @PostMapping("/api/board/{boardId}/reply")
    //public ResponseDto<Integer> replySave(@PathVariable int boardId,@RequestBody Reply reply, @AuthenticationPrincipal PrincipalDetail principal){
    public ResponseDto<Integer> replySave(@RequestBody ReplySaveRequestDto saveRequestDto){

        //boardService.댓글쓰기(principal.getUser(),boardId,reply);

        boardService.댓글쓰기(saveRequestDto);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }

    @DeleteMapping("/api/board/{boardId}/reply/{replyId}")
    public ResponseDto<Integer> replyDete(@PathVariable int replyId){
        boardService.댓글삭제(replyId);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }



}
