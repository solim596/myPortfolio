package com.example.myPortfolio.service;

import com.example.myPortfolio.board.Board;
import com.example.myPortfolio.repository.BoardRepository;
import com.example.myPortfolio.user.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    // board테이블의 전체 목록 가져오기
    public List<Board> getLists() {
        return this.boardRepository.findAll();
    }

    // board테이블에서 id에 해당하는 레코드 1개만 가져오기
    public Board getOne(Integer id) {
        // boardRepositor의 findById메서드를 사용하여 id에 해당하는 레코드를 board테이블에서 가져와서 board인스턴스에 저장
        // board.id, board.subject, board.content, board.createDate, board.modifyDate값을 저장
        Optional<Board> board = this.boardRepository.findById(id);
        // board테이블에서 얻어온 데이터들을 getOne()메서드를 호출한 곳으로 반환함.
        return board.get();
    }

    // 글쓰기
    public void create(String subject, String content, SiteUser author) {
        // Board형식의 board인스턴스 생성
        Board board = new Board();
        // board인스턴스에 매개변수로 전달받은 제목(subject) 세팅
        board.setSubject(subject);
        // board인스턴스에 매개변수로 전달받은 내용(content) 세팅
        board.setContent(content);
        // board인스턴스에 매개변수로 전달받은 사용자 아이디(author) 세팅
        board.setAuthor(author);
        // boardRepository를 통해서 DB의 board테이블에 제목, 내용 저장
        this.boardRepository.save(board);
    }

    // 글 수정하기
    public void modify(Board board, String subject, String content) {
        // 매개변수로 전달받은 제목(subject)을 board인스턴스에 세팅
        board.setSubject(subject);
        // 매개변수로 전달받은 내용(content)을 board인스턴스에 세팅
        board.setContent(content);
        // 수정 날짜를 현재 날짜로 세팅
        board.setModifyDate(LocalDate.now());
        // DB의 board테이블에 subject, content, modifyDate값 저장
        this.boardRepository.save(board);
    }

    // 글 삭제하기
    public void delete(Board board) {
        this.boardRepository.delete(board);
    }

    // 페이징
    public Page<Board> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Board> spec = search(kw);
        return this.boardRepository.findAll(spec, pageable);
    }

    // 게시글 검색 기능
    // Specification : JPA에서 동적 쿼리를 만들기 위한 객체
    private Specification<Board> search(String kw){
        return new Specification<Board>() {
            private static final long serialVersionUID = 1;
            // Predicate : 동적 쿼리문에 논리연산자(and, or)를 사용하려고 할때 만드는 객체
            // Critetia API : 기존 쿼리 만드는 방식에서는 오류를 검출할 수 없기 때문에 Criteria API를 사용하여 오류를 검출하고, 동적 쿼리를 생성할때 객체 지향적으로 만듬.
            @Override
            public Predicate toPredicate(Root<Board> b, CriteriaQuery<?> query, CriteriaBuilder cb){
                query.distinct(true);  //중복제거
                Join<Board, SiteUser> u1 = b.join("author", JoinType.LEFT);
                Join<Board, Comment> c = b.join("commentList", JoinType.LEFT);
                Join<Comment, SiteUser> u2 = c.join("author", JoinType.LEFT);
                return cb.or(cb.like(b.get("subject"), "%" + kw + "%"),
                        cb.like(b.get("content"), "%" + kw + "%"),
                        cb.like(u1.get("username"), "%" + kw + "%"),
                        cb.like(c.get("content"), "%" + kw + "%"),
                        cb.like(u2.get("username"), "%" + kw + "%"));
            }
        };
    }
}