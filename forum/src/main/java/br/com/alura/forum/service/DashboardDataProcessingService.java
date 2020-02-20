package br.com.alura.forum.service;

import br.com.alura.forum.controller.dto.data.CategoriesAndTheirStatisticsData;
import br.com.alura.forum.controller.dto.data.CategoryStatisticsData;
import br.com.alura.forum.model.Category;
import br.com.alura.forum.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardDataProcessingService {

    private CategoryRepository categoryRepository;
    private CategoryStatisticsDataLoadingService categoryStatisticsService;

    @Autowired
    public DashboardDataProcessingService(CategoryRepository categoryRepository,
                                          CategoryStatisticsDataLoadingService categoryStatisticsService) {
        this.categoryRepository = categoryRepository;
        this.categoryStatisticsService = categoryStatisticsService;
    }

    public CategoriesAndTheirStatisticsData execute() {
        List<Category> principalCategories = this.categoryRepository.findByCategoryIsNull();

        CategoriesAndTheirStatisticsData categoriesAndTheirData = new CategoriesAndTheirStatisticsData();

        principalCategories.stream().forEach(category -> {
            CategoryStatisticsData statisticsData = this.categoryStatisticsService.load(category);
            categoriesAndTheirData.add(category, statisticsData);
        });

        return categoriesAndTheirData;
    }
}
