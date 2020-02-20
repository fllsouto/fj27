package br.com.alura.forum.repository;

import br.com.alura.forum.model.topic.domain.Topic;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicRepository extends Repository<Topic, Long>, JpaSpecificationExecutor<Topic> {

    // Alternativa ao findAll
    //@Query("select t from Topic t")
    //List<Topic> list();

    List<Topic> findAll();

    @Query("select t from Topic t where t.course.subcategory.name in :subcategoryNames")
    List<Topic> findByCourseCategoryName(@Param("subcategoryNames") List<String> subcategoryNames);
}
