package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.input.NewAnswerInputDto;
import br.com.alura.forum.controller.dto.output.AnswerOutputDto;
import br.com.alura.forum.model.Answer;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.AnswerRepository;
import br.com.alura.forum.repository.TopicRepository;
import br.com.alura.forum.service.NewReplyProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/topics/{topicId}/answers")
public class AnswerController {

    private final Logger logger = LoggerFactory.getLogger(AnswerController.class);

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private NewReplyProcessorService newReplyProcessorService;

    @CacheEvict(value = "topicDetails", key = "#topicId")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnswerOutputDto> createAnswer(@PathVariable Long topicId,
        @RequestBody @Valid NewAnswerInputDto newAnswerDto,
        @AuthenticationPrincipal User loggedUser,
        UriComponentsBuilder uriComponentsBuilder) {
        logger.info("Executando criação de nova resposta na Thread de ID: " + Thread.currentThread().getId());

        Optional<Topic> optionalTopic = topicRepository.findById(topicId);
        Topic topic = optionalTopic.orElseThrow(() -> new RuntimeException("Tópico não encontrado!"));

        Answer answer = newAnswerDto.build(topic, loggedUser);
        newReplyProcessorService.execute(answer);

        URI path = uriComponentsBuilder
                .path("/api/topics/{topicId}/answers/{answerId}")
                .buildAndExpand(topicId, answer.getId())
                .toUri();

        return ResponseEntity.created(path).body(new AnswerOutputDto(answer));
    }
}
