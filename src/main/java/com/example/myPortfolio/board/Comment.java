package com.example.myPortfolio.board;

import com.example.myPortfolio.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "TEXT")
    private String content;
    @CreationTimestamp
    private LocalDate createDate;
    @UpdateTimestamp
    private LocalDate updateDate;
    // 하나의 게시글(board)에 여러개의 댓글(comment)작성 가능
    @ManyToOne
    private Board board;

    // 사용자 1명이 댓글을 여러개 작성 가능
    @ManyToOne
    private SiteUser author;
}
