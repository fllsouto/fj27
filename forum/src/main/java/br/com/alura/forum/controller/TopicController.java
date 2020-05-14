package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.data.CategoriesAndTheirStatisticsData;
import br.com.alura.forum.controller.dto.input.NewTopicInputDto;
import br.com.alura.forum.controller.dto.input.TopicSearchInputDto;
import br.com.alura.forum.controller.dto.output.TopicBriefOutputDto;
import br.com.alura.forum.controller.dto.output.TopicDashboardItemOutputDto;
import br.com.alura.forum.controller.dto.output.TopicOutputDto;
import br.com.alura.forum.infra.TopicAclPermissionRecorderService;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.CourseRepository;
import br.com.alura.forum.repository.TopicRepository;
import br.com.alura.forum.service.DashboardDataProcessingService;
import br.com.alura.forum.validator.NewTopicCustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private DashboardDataProcessingService dashboardDataProcessingService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TopicAclPermissionRecorderService aclPermissionsRecorder;

    @InitBinder("newTopicInputDto")
    public void initBinder(WebDataBinder binder, @AuthenticationPrincipal User loggedUser) {
        binder.addValidators(new NewTopicCustomValidator(this.topicRepository, loggedUser));
    }

    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public Page<TopicBriefOutputDto> listTopics(TopicSearchInputDto topicSearch,
        @PageableDefault(sort = "creationInstant", direction = Sort.Direction.DESC) Pageable pageRequest) {

        Specification<Topic> topicSearchSpecification = topicSearch.build();
        Page<Topic> topics = topicRepository.findAll(topicSearchSpecification, pageRequest);
        return TopicBriefOutputDto.listFromTopics(topics);
    }

    @Cacheable("dashboardData")
    @GetMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TopicDashboardItemOutputDto> getDashboardInfo() {
        CategoriesAndTheirStatisticsData categoriesStatisticsData = this.dashboardDataProcessingService.execute();
        return TopicDashboardItemOutputDto.listFromCategories(categoriesStatisticsData);
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TopicOutputDto> createTopic(@Valid @RequestBody NewTopicInputDto newTopicDto,
        @AuthenticationPrincipal User loggedUser, UriComponentsBuilder uriBuilder) {

        Topic topic = newTopicDto.build(loggedUser, courseRepository);
        this.topicRepository.save(topic);

        aclPermissionsRecorder.addPermission(topic, BasePermission.ADMINISTRATION);

        URI path = uriBuilder.path("/api/topics/{id}").buildAndExpand(topic.getId()).toUri();
        return ResponseEntity.created(path).body(new TopicOutputDto(topic));
    }

    @GetMapping("/{topicId}")
    @Cacheable(value = "topicDetails", key="#topicId")
    public TopicOutputDto getTopicDetails(@PathVariable Long topicId) {
        System.out.println("Executando o getTopicDetails");
        Optional<Topic> optionalTopic = topicRepository.findById(topicId);
        Topic topic = optionalTopic.orElseThrow(() -> new RuntimeException("Tópico não encontrado!"));
        return new TopicOutputDto(topic);
    }

    @Transactional
    @CacheEvict(value = "topicDetails", key = "#topicId")
    @PostMapping("/{topicId}/close")
    @PreAuthorize(
            "hasPermission(#topicId, 'br.com.alura.forum.model.topic.domain.Topic', 'ADMINISTRATION')"
    )
    public ResponseEntity<Void> closeTopic(@PathVariable("topicId") Long topicId, UriComponentsBuilder uriBuilder) {

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
        topic.close();

        URI uri = uriBuilder.path("/api/topics/{topicId}/closed").buildAndExpand(topicId).toUri();

        return ResponseEntity.noContent().location(uri).build();
    }

}
