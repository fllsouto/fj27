package br.com.alura.forum.controller;

import br.com.alura.forum.model.Category;
import br.com.alura.forum.model.Course;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@Controller
public class TopicController {

    @ResponseBody
    @RequestMapping("/api/topics")
    public List<Topic> listTopics() {
        Category subcategory = new Category("Java", new Category("Programação"));
        Course course = new Course("Java e JSF", subcategory);
        Topic topic = new Topic("Problemas com JSF",
                "Erro ao fazer conversão da data",
                new User("Fulano", "fulano@gmail.com", "123456"), course);

        List<Topic> topics = Arrays.asList(topic, topic, topic);
        return topics;
    }
}
