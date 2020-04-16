package br.com.alura.forum.task;

import br.com.alura.forum.model.OpenTopicByCategory;
import br.com.alura.forum.repository.OpenTopicByCategoryRepository;
import br.com.alura.forum.repository.TopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegisterUnansweredTopicTask {

    private static final Logger logger = LoggerFactory.getLogger(RegisterUnansweredTopicTask.class);

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private OpenTopicByCategoryRepository openTopicByCategoryRepository;

    // Execução as 20hrs, todos os dias
    //@Scheduled(cron = "0 0 20 * * *")

    // Execução de 10 em 10 segundos
    @Scheduled(cron = "*/10 * * * * *")
    public void execute() {
        logger.info("Gerando relatório de tópicos não respondidos...");
        List<OpenTopicByCategory> topics = topicRepository
                .findOpenTopicsByCategory();
        openTopicByCategoryRepository.saveAll(topics);
    }
}
