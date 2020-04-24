package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.input.NewAnswerInputDto;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.TopicRepository;
import br.com.alura.forum.repository.UserRepository;
import br.com.alura.forum.security.jwt.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriTemplate;

import javax.transaction.Transactional;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AnswerControllerTest {

    private static final String ENDPOINT = "/api/topics/{topicId}/answers";

    private Long topicId;
    private String jwt;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenManager tokenManager;

    @Before
    public void setup() throws RuntimeException {
        String rawPassword = "123456";
        User user = new User("Aluno da Alura", "aluno@gmail.com",
                new BCryptPasswordEncoder().encode(rawPassword));
        User persistedUser = this.userRepository.save(user);

        Topic topic = new Topic("Descrição do Tópico", "Conteúdo do Tópico", persistedUser, null);

        Topic persistedTopic = topicRepository.save(topic);
        this.topicId = persistedTopic.getId();

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(persistedUser.getEmail(), rawPassword));
        this.jwt = this.tokenManager.generateToken(authentication);
    }

    @Test
    public void shouldProcessSuccessfullyNewAnswerRequest() throws Exception {
        URI uri = new UriTemplate(ENDPOINT).expand(this.topicId);

        NewAnswerInputDto inputDto = new NewAnswerInputDto();
        inputDto.setContent("Não consigo subir o servidor");
        String valueAsString = new ObjectMapper().writeValueAsString(inputDto);
        MockHttpServletRequestBuilder request = post(uri).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + this.jwt)
                .content(valueAsString);


        this.mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andDo(result -> result.getResponse().setCharacterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(content().string(containsString(inputDto.getContent())));
    }
}