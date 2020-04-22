package br.com.alura.forum.repository;

import br.com.alura.forum.model.Category;
import br.com.alura.forum.model.Course;
import br.com.alura.forum.model.topic.domain.Topic;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class TopicRepositoryTestSetup {

    private final TestEntityManager testEntityManager;

    public TopicRepositoryTestSetup(TestEntityManager testEntityManager) {
        this.testEntityManager = testEntityManager;
    }

    public void openTopicsByCategorySetup() {
        Category programacao = this.testEntityManager
                .persist(new Category("Programação"));
        Category front = this.testEntityManager
                .persist(new Category("Front-end"));

        Category javaweb = this.testEntityManager
                .persist(new Category("Java Web", programacao));
        Category javascript = this.testEntityManager
                .persist(new Category("Javascript", front));

        Course fj27 = this.testEntityManager
                .persist(new Course("Spring Framework", javaweb));
        Course fj21 = this.testEntityManager
                .persist(new Course("Servlet API e MVC", javaweb));
        Course js46 = this.testEntityManager
                .persist(new Course("React", javascript));

        Topic springTopic = new Topic("Tópico Spring", "Conteúdo do tópico", null, fj27);
        Topic servletTopic = new Topic("Tópico Servlet", "Conteúdo do tópico", null, fj21);
        Topic reactTopic = new Topic("Tópico React", "Conteúdo do tópico", null, js46);

        this.testEntityManager.persist(springTopic);
        this.testEntityManager.persist(servletTopic);
        this.testEntityManager.persist(reactTopic);
    }
}
