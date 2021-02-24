package me.ablax.unigram.repositories;

import me.ablax.unigram.entities.Photo;
import me.ablax.unigram.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Murad Hamza on 23.2.2021 г.
 */
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findAllByOwner(final User owner);
}
