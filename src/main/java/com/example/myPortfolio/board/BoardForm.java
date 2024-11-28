package com.example.myPortfolio.board;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardForm {
    @NotEmpty(message = "제목을 입력해 주세요.")
    @Size(max=200)
    private String subject;
    @NotEmpty(message = "내용을 입력해 주세요")
    private String content;
}
