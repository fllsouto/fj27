package br.com.alura.forum.controller.dto.output;

import br.com.alura.forum.model.topic.domain.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicWithAnswersOutputDto extends TopicOutputDto{

    private List<AnswerOutputDto> answers = new ArrayList<AnswerOutputDto>();

    public TopicWithAnswersOutputDto(Topic topic) {
        super(topic);
        topic.getAnswers().stream().map(AnswerOutputDto::new).forEach(this::addAnswer);
    }

    public void addAnswer(AnswerOutputDto answer) {
        this.answers.add(answer);
    }

    public List<AnswerOutputDto> getAnswers() {
        return answers;
    }
}
