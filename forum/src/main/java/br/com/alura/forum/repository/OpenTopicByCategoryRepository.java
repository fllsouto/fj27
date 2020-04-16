package br.com.alura.forum.repository;

import br.com.alura.forum.model.OpenTopicByCategory;
import org.springframework.data.repository.Repository;

public interface OpenTopicByCategoryRepository
        extends Repository<OpenTopicByCategory, Long> {

    void saveAll(Iterable<OpenTopicByCategory> topics);
}
