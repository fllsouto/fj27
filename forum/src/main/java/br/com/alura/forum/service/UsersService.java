package br.com.alura.forum.service;

import br.com.alura.forum.model.User;
import br.com.alura.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> possibleUser = userRepository
                .findByEmail(username);

        return possibleUser.orElseThrow(() -> new UsernameNotFoundException("Não foi possível enontrar o usuário com o email : " + username));
    }

    public UserDetails loadUserById(Long userId) {
        Optional<User> possibleUser = userRepository.findById(userId);

        return possibleUser.orElseThrow(() -> new UsernameNotFoundException("Não foi possível enontrar o usuário com o id : " + userId));
    }
}
