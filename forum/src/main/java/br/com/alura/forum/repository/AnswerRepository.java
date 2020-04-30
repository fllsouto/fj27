package br.com.alura.forum.repository;

import br.com.alura.forum.model.Answer;
import org.springframework.data.repository.Repository;

public interface AnswerRepository extends Repository<Answer, Long> {

    void save(Answer answer);

    Answer findById(Long answerId);
}
