package br.com.alura.forum.service;

import br.com.alura.forum.controller.dto.data.CategoryStatisticsData;
import br.com.alura.forum.model.Category;
import br.com.alura.forum.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class CategoryStatisticsDataLoadingService {

    private TopicRepository topicRepository;

    @Autowired
    public CategoryStatisticsDataLoadingService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public CategoryStatisticsData load(Category category) {

        int allTopics = this.topicRepository.countTopicsByCategory(category);

        Instant lastWeek = Instant.now().minus(7, ChronoUnit.DAYS);
        int lastWeekTopics = this.topicRepository.countLastWeekTopicsByCategory(category, lastWeek);

        int unansweredTopics = this.topicRepository.countUnansweredTopicsByCategory(category);

        return new CategoryStatisticsData(allTopics, lastWeekTopics, unansweredTopics);
    }
}
