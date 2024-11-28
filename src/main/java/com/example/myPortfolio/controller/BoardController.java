package com.example.myPortfolio.controller;

import com.example.myPortfolio.board.Board;
import com.example.myPortfolio.board.BoardForm;
import com.example.myPortfolio.board.CommentForm;
import com.example.myPortfolio.service.BoardService;
import com.example.myPortfolio.service.UserService;
import com.example.myPortfolio.user.SiteUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
        // List<Board> : 자료형
        // boardList : 인스턴스명(변수명)
        // this : 현재 메서드를 가리킴
        // . : ~에 속한
        // boardService객체의 getLists() 메서드 호출한 결과값을  boardList에 저장(id, subject, content, create_date, update_date)
        Page<Board> paging = this.boardService.getList(page, kw);
        // Model객체는 controller에서 가져온 목록을 view로 전달함
        model.addAttribute("paging", paging);
        return "board/board_list";
    }
    // 상세 페이지 보기, {id}는 board_list.html에서 전달한 매개변수 ${board.id}값을 받는 변수이다.
    // @PathVariable("id")는 {id}에서 전달받은 id값을 매개변수로 전달 받음
    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, CommentForm commentForm) {
        // boardService의 getOne()메서드를 호출하고, 그 결과를 board인스턴스에 저장
        Board board = this.boardService.getOne(id);
        // board인스턴스의 데이터를 board_detail.html로 전달하기 위해 model객체에 속성으로 세팅
        model.addAttribute("board", board);
        return "board/board_detail";
    }
    // 글쓰기
    @GetMapping("/create")
    public String create(BoardForm boardForm) {
        return "board/board_create";
    }

    // @RequestParam(value="subject") : 사용자가 board_create.html문서에서 작성한 제목을 전달받는 매개변수
    // @RequestParam(value="content") : 사용자가 board_create.html 문서에서 작성한 내용을 전달받는 매개변수
    @PostMapping("/create")
    public String create(@Valid BoardForm boardForm, BindingResult bindingResult, Principal principal) {
        SiteUser siteUser = this.userService.getUser(principal.getName());
        // subject,content의 유효성 검사에서 에러 있으면 그 결과를 담아서 board_create.html에 전달
        if(bindingResult.hasErrors()) {
            return "board/board_create";
        }
        // boardService의 create메서드 호출
        // boardForm.getSubject() : 유효성 검사가 끝난 제목 데이터
        // boardForm.getContent() : 유효성 검사가 끝난 내용 데이터
        this.boardService.create(boardForm.getSubject(), boardForm.getContent(), siteUser);
        // 제목과 내용을 boardService에 전달하고 나소 목록페이지로 이동
        return "redirect:/board/list";
    }
    
    // 게시글 수정하기
    // boardForm : 유효성 검사를 위한 인스턴스 객체
    // PathVariable("id") Integer id : 매개변수로 id값 전달받음
    @GetMapping("/modify/{id}")
    public String modify(BoardForm boardForm, @PathVariable("id")
        Integer id) {
        // boardService의 getOne()메서드를 호출해거 id값에 해당하는 레코드를 board테이블에서 가져와서 board인스턴스에 저장
        Board board = this.boardService.getOne(id);
        // 만약 로그인된 아이디와 글 작성한 아이디가 같지 않으면 수정관한 없음

        // board테이블에서 가져온 subject, content를 boardForm에 세팅해서 유효성 검사함
        boardForm.setSubject(board.getSubject());
        boardForm.setContent(board.getContent());
        // board폴더의 board_create.html로 이동
        return "board/board_create";
    }

    @PostMapping("/modify/{id}")
    public String modify(@Valid BoardForm boardForm, BindingResult bindingResult, @PathVariable("id") Integer id) {
        // 유효성 검사에서 에러가 생기면 예외처리
        if(bindingResult.hasErrors()) {
            return "board/board_create";
        }
        // boardService의 getOne()메서드를 호출하여 id값에 해당하는 subject, content데이터를 가져온 다음 board인스턴스에 저장
        Board board = this.boardService.getOne(id);
        
        // 만약 로그인한 아이디와 글쓴 아이디가 같지 않으면 수정권한이 없습니다.


        // boardService의 modify() 메서드 호출
        this.boardService.modify(board, boardForm.getSubject(), boardForm.getContent());
        
        // id값을 가지고 있는 게시글의 상세 보기로 이동
        return String.format("redirect:/board/detail/%s", id);
    }

    // 글 삭제하기
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        Board board = this.boardService.getOne(id);
        // 로그인한 아이디와 글쓴 아이디가 같지 않으면 삭제 권한이 없습니다.


        this.boardService.delete(board);
        return "redirect:/board/list";
    }
}
