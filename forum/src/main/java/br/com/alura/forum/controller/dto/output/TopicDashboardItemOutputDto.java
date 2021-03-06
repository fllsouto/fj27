package br.com.alura.forum.controller.dto.output;

import br.com.alura.forum.controller.dto.data.CategoriesAndTheirStatisticsData;
import br.com.alura.forum.controller.dto.data.CategoryStatisticsData;
import br.com.alura.forum.model.Category;

import java.io.Serializable;
import java.util.List;

public class TopicDashboardItemOutputDto implements Serializable {

    private String categoryName;
    private List<String> subcategories;
    private int allTopics;
    private int lastWeekTopics;
    private int unansweredTopics;
    private static final long serialVersionUID = 1808919651999253689L;

    public TopicDashboardItemOutputDto(Category category, CategoryStatisticsData numbers) {
        this.categoryName = category.getName();
        this.subcategories = category.getSubcategoryNames();
        this.allTopics = numbers.getAllTopics();
        this.lastWeekTopics = numbers.getLastWeekTopics();
        this.unansweredTopics = numbers.getUnansweredTopics();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public int getAllTopics() {
        return allTopics;
    }

    public int getLastWeekTopics() {
        return lastWeekTopics;
    }

    public int getUnansweredTopics() {
        return unansweredTopics;
    }

    public static List<TopicDashboardItemOutputDto> listFromCategories(CategoriesAndTheirStatisticsData categoriesStatisticsData) {
        return categoriesStatisticsData
                .map((category, statData) -> new TopicDashboardItemOutputDto(category, statData));
    }

}
