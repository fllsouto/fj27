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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
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

    @Autowired
    private AnswerRepository answerRepository;

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

    @Transactional
    @CacheEvict(value = "topicDetails", key = "#topicId")
    @PostMapping("/{answerId}/solution")
    @PreAuthorize(
            "hasPermission(#topicId, 'br.com.alura.forum.model.topic.domain.Topic', 'ADMINISTRATION')"
    )
    public ResponseEntity<?> markAsSolution(@PathVariable("topicId") Long topicId, @PathVariable("answerId") Long answerId,
        UriComponentsBuilder uriBuilder, @AuthenticationPrincipal User loggedUser) {

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado!"));

     //   if(loggedUser.isOwnerOf(topic) || loggedUser.isAdmin()) {

            Answer answer = answerRepository.findById(answerId);
            answer.markAsSolution();

            URI uri = uriBuilder.path("/api/topics/{topicId}/solution")
                    .buildAndExpand(topicId)
                    .toUri();
            return ResponseEntity.created(uri).body(new AnswerOutputDto(answer));
       // }

        // return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem direito a acessar este recurso!");
    }
}
