package br.com.alura.forum.controller.dto.input;

import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.model.topic.domain.TopicStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;

public class TopicSearchInputDto {

    private TopicStatus status;
    private String categoryName;

    public TopicStatus getStatus() {
        return status;
    }

    public void setStatus(TopicStatus status) {
        this.status = status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    // Solução com expressão Lambda
    public Specification<Topic> build() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            ArrayList<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (categoryName != null) {
                Path<String> categoryNamePath = root.get("course").get("subcategory")
                        .get("category").get("name");
                predicates.add(criteriaBuilder.equal(categoryNamePath, categoryName));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<Topic> buildWithAnonymousClass() {
        return new Specification<Topic>() {
            @Override
            public Predicate toPredicate(Root<Topic> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicates = new ArrayList<>();

                if (status != null) {
                    predicates.add(criteriaBuilder.equal(root.get("status"), status));
                }

                if (categoryName != null) {
                    Path<String> categoryNamePath = root.get("course").get("subcategory")
                            .get("category").get("name");
                    predicates.add(criteriaBuilder.equal(categoryNamePath, categoryName));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }

    public Specification<Topic> buildWithInnerClass() {
        return new TopicSearchSpecificationImpl();
    }

    class TopicSearchSpecificationImpl implements Specification<Topic> {

        @Override
        public Predicate toPredicate(Root<Topic> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            ArrayList<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (categoryName != null) {
                Path<String> categoryNamePath = root.get("course").get("subcategory")
                        .get("category").get("name");
                predicates.add(criteriaBuilder.equal(categoryNamePath, categoryName));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }
    }
}
