package com.example.myPortfolio.controller;

import com.example.myPortfolio.board.Board;
import com.example.myPortfolio.board.Comment;
import com.example.myPortfolio.board.CommentForm;
import com.example.myPortfolio.service.BoardService;
import com.example.myPortfolio.service.CommentService;
import com.example.myPortfolio.service.UserService;
import com.example.myPortfolio.user.SiteUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/comment")
@Controller
public class commentController {
    private final BoardService boardService;
    private final CommentService commentService;
    private  final UserService userService;
    // 댓글 작성하기
    @GetMapping("/create/{id}")
    public String create(CommentForm commentForm) {
        return "board/board_detail";
    }
    @PostMapping("/create/{id}")
    public String create(@Valid CommentForm commentForm, BindingResult bindingResult, Model model, @PathVariable("id") Integer id, Principal principal) {
        // 매개변수 id에 해당하는 게시글 가져와서 board인스턴스에 저장
        Board board = this.boardService.getOne(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        // 유효성 검사 중 에러 발생하면 그 결과를 저장
        if(bindingResult.hasErrors()) {
            // board_detail.html페이지에 board속성 추가
            model.addAttribute("board", board);
            return "board/board_detail";
        }
        
        // 게시글 데이터(board_id)와 댓글 내용을 저장하기 위해 commentService의 create()메서드 호출
        this.commentService.create(board, commentForm.getContent(), siteUser);
        // 댓글이 달린 게시글의 상세보기로 이동
        return String.format("redirect:/board/detail/%s", id);
    }

    // 댓글 1개 가져오기
    @GetMapping("/modify/{id}")
    public String modify(CommentForm commentForm, @PathVariable("id") Integer id) {
        // 매개변수로 전달받은 id값에 해당하는 댓글을 가져와서 comment에 저장
        Comment comment = this.commentService.getOne(id);
        // 유효성 검사된 댓글 내용을 세팅
        commentForm.setContent(comment.getContent());
        // board폴더 안의 comment_form.html 페이지로 이동
        return "comment/comment_form";
    }
    // @Valid CommentForm commentForm : commentForm을 사용하여 유효성 검사함
    // BindingResult : 유효성 검사하다가 오류 있으면 예외처리 결과 저장 객체
    // @PathVairable("id") Integer id : 매개변수{id}로 전달받은 id값
    @PostMapping("modify/{id}")
    public String modify(@Valid CommentForm commentForm, BindingResult bindingResult, @PathVariable("id") Integer id) {
        // 유효성 검사에서 에러가 생기면 그 결과를 가지고 board폴더 안의 comment_form.html페이지로 이동
        if(bindingResult.hasErrors()) {
            return "comment/comment_form";
        }
        // commentService의 getOne메서드를 호출하면서 id값을 매개변수로 전달
        // id값으로 comment테이블에서 id값에 해당하는 레코드를 1개 받아서 comment인스턴스에 저장
        Comment comment = this.commentService.getOne(id);
        // comment에 있는 댓글을 modify메서드로 전달
        this.commentService.modify(comment, commentForm.getContent());
        // board테이블의 id와 comment테이블의 board_id가 같은 레코드를 가지고 board/detail 매핑주소로 이동. %s는 문자열형임
        return String.format("redirect:/board/detail/%s", comment.getBoard().getId());
    }

    // 댓글 삭제하기
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        // commentService의 getOne메서드를 호출하면서 id를 매개변수로 전달
        // getOne메서드에 의해 가져온 댓글을 comment인스턴스에 저장
        Comment comment = this.commentService.getOne(id);
        // commentService의 delete메서드 호출하면서 댓글을 매개변수로 전달
        this.commentService.delete(comment);
        // 댓글이 지워진 게시글의 상세보기 페이지로 이동
        return String.format("redirect:/board/detail/%s", comment.getBoard().getId());
    }
}
