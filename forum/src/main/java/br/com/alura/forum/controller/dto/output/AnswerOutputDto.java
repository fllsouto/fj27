package br.com.alura.forum.controller.dto.output;

import br.com.alura.forum.model.Answer;

import java.io.Serializable;
import java.time.Instant;

public class AnswerOutputDto implements Serializable {

    public Long id;
    public String content;
    public Instant creationTime;
    public boolean solution;
    public String ownerName;

    private static final long serialVersionUID = -6993596629533213092L;

    public AnswerOutputDto(Answer answer) {
        this.id = answer.getId();
        this.content = answer.getContent();
        this.creationTime = answer.getCreationTime();
        this.solution = answer.isSolution();
        this.ownerName = answer.getOwnerName();
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public boolean isSolution() {
        return solution;
    }

    public String getOwnerName() {
        return ownerName;
    }
}
