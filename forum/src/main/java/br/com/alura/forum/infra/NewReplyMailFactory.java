package br.com.alura.forum.infra;

import br.com.alura.forum.model.Answer;
import br.com.alura.forum.model.topic.domain.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class NewReplyMailFactory {

    @Autowired
    private TemplateEngine templateEngine;

    public String generateNewReplyMailContent(Answer answer) {
        Topic answerTopic = answer.getTopic();

        Context thymeleafContext = new Context();
        thymeleafContext.setVariable("topicOwnerName",
                answerTopic.getOwner());
        thymeleafContext.setVariable("topicShortDescription",
                answerTopic.getShortDescription());
        thymeleafContext.setVariable("answerAuthor",
                answer.getOwnerName());
        thymeleafContext.setVariable("answerCreationInstant",
                getFormattedCreationTime(answer));
        thymeleafContext.setVariable("answerContent",
                answer.getContent());

        return this.templateEngine.process("email-template.html", thymeleafContext);
    }

    private String getFormattedCreationTime(Answer answer) {
        return DateTimeFormatter.ofPattern("kk:mm")
                .withZone(ZoneId.of("America/Sao_Paulo"))
                .format(answer.getCreationTime());
    }
}
