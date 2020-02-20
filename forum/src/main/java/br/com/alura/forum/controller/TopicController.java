package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.input.TopicSearchInputDto;
import br.com.alura.forum.controller.dto.output.CategoryTopicsInfoOutputDto;
import br.com.alura.forum.controller.dto.output.TopicBriefOutputDto;
import br.com.alura.forum.model.Category;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.CategoryRepository;
import br.com.alura.forum.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping(value = "/api/topics", produces=MediaType.APPLICATION_JSON_VALUE)
    public Page<TopicBriefOutputDto> listTopics(TopicSearchInputDto topicSearch,
                                                @PageableDefault(sort = "creationInstant", direction = Sort.Direction.DESC) Pageable pageRequest) {

        Specification<Topic> topicSearchSpecification = topicSearch.build();
        Page<Topic> topics = topicRepository.findAll(topicSearchSpecification, pageRequest);
        return TopicBriefOutputDto.listFromTopics(topics);
    }

    @GetMapping("/api/topics/dashboard")
    public List<CategoryTopicsInfoOutputDto> listCategoryTopics() {

        List<Category> mainCategories = categoryRepository.findMainCategories();

        List<CategoryTopicsInfoOutputDto> dto = mainCategories.stream().map(mainCategory -> {
            List<String> subcategoryNames = mainCategory.getSubcategoryNames();
            List<Topic> topicsFromMainCategory = topicRepository.findByCourseCategoryName(subcategoryNames);

            return new CategoryTopicsInfoOutputDto(mainCategory, topicsFromMainCategory);
        }).collect(Collectors.toList());

        return dto;
    }
}
