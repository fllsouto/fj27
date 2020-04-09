package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.data.CategoriesAndTheirStatisticsData;
import br.com.alura.forum.controller.dto.input.NewAnswerInputDto;
import br.com.alura.forum.controller.dto.input.NewTopicInputDto;
import br.com.alura.forum.controller.dto.input.TopicSearchInputDto;
import br.com.alura.forum.controller.dto.output.*;
import br.com.alura.forum.model.Answer;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.AnswerRepository;
import br.com.alura.forum.repository.CourseRepository;
import br.com.alura.forum.repository.TopicRepository;
import br.com.alura.forum.service.DashboardDataProcessingService;
import br.com.alura.forum.validator.NewTopicCustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
    private AnswerRepository answerRepository;

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

    @GetMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TopicDashboardItemOutputDto> getDashboardInfo() {
        CategoriesAndTheirStatisticsData categoriesStatisticsData = this.dashboardDataProcessingService.execute();
        return TopicDashboardItemOutputDto.listFromCategories(categoriesStatisticsData);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TopicOutputDto> createTopic(@Valid @RequestBody NewTopicInputDto newTopicDto,
        @AuthenticationPrincipal User owner, UriComponentsBuilder uriBuilder) {

        Topic topic = newTopicDto.build(owner, courseRepository);
        this.topicRepository.save(topic);
        URI path = uriBuilder.path("/api/topics/{id}").buildAndExpand(topic.getId()).toUri();
        return ResponseEntity.created(path).body(new TopicOutputDto(topic));
    }

    @GetMapping("/{topicId}")
    public TopicWithAnswersOutputDto getTopic(@PathVariable("topicId") Long topicId) {
        Optional<Topic> optionalTopic = topicRepository.findById(topicId);

        Topic topic = optionalTopic.orElseThrow(() -> new RuntimeException("Tópico não encontrado!"));

        return new TopicWithAnswersOutputDto(topic);
    }

    @PostMapping(value = "/{topicId}/answers",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnswerOutputDto> createAnswer(@PathVariable("topicId") Long topicId,
        @RequestBody @Valid NewAnswerInputDto newAnswerDto,
        @AuthenticationPrincipal User loggedUser,
        UriComponentsBuilder uriComponentsBuilder) {

        Optional<Topic> optionalTopic = topicRepository.findById(topicId);
        Topic topic = optionalTopic.orElseThrow(() -> new RuntimeException("Tópico não encontrado!"));

        Answer answer = new Answer(newAnswerDto.getContent(), topic, loggedUser);
        answerRepository.save(answer);

        URI path = uriComponentsBuilder.path("/api/topics/{topicId}").buildAndExpand(topicId).toUri();
        return ResponseEntity.created(path).body(new AnswerOutputDto(answer));
    }
}
