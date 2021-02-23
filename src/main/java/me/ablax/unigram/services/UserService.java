package me.ablax.unigram.services;

/**
 * @author Murad Hamza on 23.2.2021 г.
 */

import me.ablax.unigram.entities.Photo;
import me.ablax.unigram.entities.User;
import me.ablax.unigram.models.UserDto;
import me.ablax.unigram.repositories.PhotoRepository;
import me.ablax.unigram.repositories.UserRepository;
import me.ablax.unigram.utils.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    public UserService(final UserRepository userRepository, final PhotoRepository photoRepository) {
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
    }

    public UserDto registerUser(final String username, final String password, final String email) {
        if (doesUserExist(username, email)) {
            throw new RuntimeException("User exists");
        }
        final User user = new User();
        user.setUsername(username);
        user.setEmail(email);

        String generatedSecuredPasswordHash = BCrypt.hashpw(password.trim(), BCrypt.gensalt(10));

        user.setPassword(generatedSecuredPasswordHash);

        return userRepository.save(user).toDto();
    }

    public boolean doesUserExist(final String username, final String email) {
        return userRepository.findByEmailOrUsername(username, username) != null || userRepository.findByEmailOrUsername(email, email) != null;
    }

    public User findById(final Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public UserDto loginUser(final String username, final String password) {
        final User user = userRepository.findByEmailOrUsername(username, username);
        if (user == null) {
            throw new RuntimeException("Invalid credentials!");
        }

        final boolean checkpw = BCrypt.checkpw(password.trim(), user.getPassword());
        if (!checkpw) {
            throw new RuntimeException("Invalid credentials!");
        }
        return user.toDto();
    }

    public List<Long> getLikedPhotosIds(final Long userId) {
        final User one = userRepository.getOne(userId);
        return one.getLikedPhotos().stream().map(Photo::getId).collect(Collectors.toList());
    }

    public boolean toggleLike(final long picId, final UserDto userDto) {
        final Photo one = photoRepository.getOne(picId);
        final User user = findById(userDto.getUserId());

        if (user.getLikedPhotos().contains(one)) {
            user.getLikedPhotos().remove(one);
            userRepository.save(user);
            return false;
        } else {
            user.getLikedPhotos().add(one);
            userRepository.save(user);
            return true;
        }
    }
}
