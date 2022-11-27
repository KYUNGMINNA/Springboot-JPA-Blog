package com.cos.blog.handler;

import com.cos.blog.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice  //모든 Exception이 발생하면 여기 클래스로 오게끔 하는 어노테이션
@RestController
public class GlobalExceptionHandler {

  /*
   @ExceptionHandler(value = Exception.class)  //특정 예외만 적으면  그 예외만 처리
    public String handleArgumentException(IllegalArgumentException e){

        return "<h1>"+e.getMessage()+"</h1>";

    }
    */

    @ExceptionHandler(value = Exception.class)  //특정 예외만 적으면  그 예외만 처리
    public ResponseDto<String> handleArgumentException(Exception e){
        return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
                                        // ResponseDto 에서 HttpStatus status; 일때 value() 생략 가능

    }

}
