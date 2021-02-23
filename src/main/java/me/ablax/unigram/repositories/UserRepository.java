package me.ablax.unigram.repositories;

import me.ablax.unigram.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Murad Hamza on 23.2.2021 г.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailOrUsername(final String email, final String username);
}
