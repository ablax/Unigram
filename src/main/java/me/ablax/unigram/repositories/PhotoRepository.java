package me.ablax.unigram.repositories;

import me.ablax.unigram.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Murad Hamza on 23.2.2021 г.
 */
public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
