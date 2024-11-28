package com.example.myPortfolio.repository;

import com.example.myPortfolio.board.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
