package com.example.myPortfolio;

import com.example.myPortfolio.board.Board;
import com.example.myPortfolio.repository.BoardRepository;
import com.example.myPortfolio.repository.UserRepository;
import com.example.myPortfolio.service.BoardService;
import com.example.myPortfolio.user.SiteUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;
import java.util.Optional;

@SpringBootTest
class MyPortfolioApplicationTests {
@Autowired
private BoardRepository boardRepository;
@Autowired
private BoardService boardService;
@Autowired
private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testJpa() {
        Board b1 = new Board();
        b1.setSubject("만나서 반가워요");
        b1.setContent("같이 공부해요");
        this.boardRepository.save(b1);
    }

    @Test
    @WithMockUser(username = "aaa")
    void testJpa2() {
        String userId = "aaa";

        Optional<SiteUser> author = this.userRepository.findByUsername(userId);
        if(author.isPresent()) {
            for (int i = 1; i <= 300; i++) {
                String subject = String.format("테스트 데이터입니다:[%03d]", i);
                String content = String.format("test data:[%03d]", i);
                this.boardService.create(subject, content, author.get());
            }
        }
    }

}
