package com.cos.blog.controller;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//Controller- 템플릿 용
@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;


    //스프링 시큐리티는 url   /logout 하면 로그아웃 됨

    @GetMapping({"","/"})   //세션에 접근하는 방법 @AuthenticationPrincipal PrincipalDetail pricipal
    public String index(Model model,@PageableDefault(size = 3,sort = "id",direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("boards",boardService.글목록(pageable));
        return "index";
        //RestController가 아닌 그냥 Controller라서  return 하면 ViewResolver(prefix + subfix)가 작동함

    }

    @GetMapping("/board/{id}")
    public String findById(@PathVariable int id,Model model){
        model.addAttribute("board",boardService.글상세보기(id));
        return "board/detail";

    }
    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id,Model model){
        model.addAttribute("board",boardService.글상세보기(id));
        return "board/updateForm";

    }

    //USER 권한이 필요
    @GetMapping("/board/saveForm")
    public String saveForm(){
        return "board/saveForm";
    }
}
