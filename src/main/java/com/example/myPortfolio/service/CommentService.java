package com.example.myPortfolio.service;

import com.example.myPortfolio.board.Board;
import com.example.myPortfolio.board.Comment;
import com.example.myPortfolio.repository.CommentRepository;
import com.example.myPortfolio.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    
    // 댓글 작성하기
    // 댓글 작성할 게시글(board)과 댓글 내용(contnet)을 매개변수로 전달받음
    public void create(Board board, String content, SiteUser user) {
        // Comment클래스로 comment인스턴스 생성
        Comment comment = new Comment();
        // Comment인스턴스에 댓글 내용 세팅
        comment.setContent(content);
        // comment가 작성되는 게시글도 comment에 세팅
        comment.setBoard(board);
        //comment를 작성하는 사용자 아이디도 세팅
        comment.setAuthor(user);
        // DB의 comment테이블 게시글 id와 댓글이 저장
        this.commentRepository.save(comment);
    }
    
    // 댓글 1개의 정보 가져오기
    public Comment getOne(Integer id) {
        // comment테이블에서 매개변수로 전달받은 id값에 해당하는 레코드를 가져와서 comment인스턴스에 저장
        Optional<Comment> comment = this.commentRepository.findById(id);
        // comment인스턴스에 들어있는 댓글 내용을 메서드 호출한 곳으로 반환
        return comment.get();
    }

    // 댓글 수정하기
    public void modify(Comment comment, String content) {
        // 매개변수로 전달받은 댓글 내용을 comment인스턴스에 세팅
        comment.setContent(content);
        // 수정날짜를 오늘날짜로 세팅
        comment.setUpdateDate(LocalDate.now());
        // DB의 comment테이블로 저장
        this.commentRepository.save(comment);
    }

    // 댓글 삭제하기
    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }

}
