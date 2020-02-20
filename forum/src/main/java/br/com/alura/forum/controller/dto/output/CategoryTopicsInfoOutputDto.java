package br.com.alura.forum.controller.dto.output;

import br.com.alura.forum.model.Category;
import br.com.alura.forum.model.topic.domain.Topic;

import java.util.List;

public class CategoryTopicsInfoOutputDto {
    private String categoryName;
    private List<String> subcategories;
    private long allTopics;
    private long lastWeekTopics;
    private long unansweredTopics;

    public CategoryTopicsInfoOutputDto(Category category, List<Topic> topics) {
        this.categoryName = category.getName();
        this.subcategories = category.getSubcategoryNames();
        this.allTopics = topics.size();
        this.unansweredTopics = topics.stream().filter(topic -> topic.isUnanswered()).count();
        this.lastWeekTopics = topics.stream().filter(topic -> topic.isOneWeekOld()).count();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public long getAllTopics() {
        return allTopics;
    }

    public long getLastWeekTopics() {
        return lastWeekTopics;
    }

    public long getUnansweredTopics() {
        return unansweredTopics;
    }
}
