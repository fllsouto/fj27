package br.com.alura.forum.repository;

import br.com.alura.forum.model.User;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long userId);
}
