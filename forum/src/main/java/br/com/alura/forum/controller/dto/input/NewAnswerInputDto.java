package br.com.alura.forum.controller.dto.input;

import javax.validation.constraints.NotBlank;

public class NewAnswerInputDto {

    @NotBlank
    private String content;

    public String getContent() {
        return content;
    }
}
