package br.com.alura.forum.repository;


import br.com.alura.forum.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface CategoryRepository extends Repository<Category, Long> {

    List<Category> findByCategoryIsNull();

    Category findByName(String string);
}
