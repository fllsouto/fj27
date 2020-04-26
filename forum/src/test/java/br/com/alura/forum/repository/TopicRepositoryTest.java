package br.com.alura.forum.repository;

import br.com.alura.forum.model.OpenTopicByCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")

// Sobrescreve o dialeto usado pelo hibernate do MySQL para o H2
//@TestPropertySource(properties = { "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"})
public class TopicRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TopicRepository topicRepository;

    @Test
    public void shouldReturnOpenTopicsByCategory() {

        TopicRepositoryTestSetup testSetup = new TopicRepositoryTestSetup(testEntityManager);
        testSetup.openTopicsByCategorySetup();

        List<OpenTopicByCategory> openTopics = this.topicRepository.findOpenTopicsByCategory();

        assertThat(openTopics).isNotEmpty();
        assertThat(openTopics).hasSize(2);

        assertThat(openTopics.get(0).getCategoryName()).isEqualTo("Programação");
        assertThat(openTopics.get(0).getTopicCount()).isEqualTo(2);

        assertThat(openTopics.get(1).getCategoryName()).isEqualTo("Front-end");
        assertThat(openTopics.get(1).getTopicCount()).isEqualTo(1);
    }
}