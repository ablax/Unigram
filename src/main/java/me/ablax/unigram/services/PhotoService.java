package me.ablax.unigram.services;

/**
 * @author Murad Hamza on 23.2.2021 г.
 */

import me.ablax.unigram.entities.Photo;
import me.ablax.unigram.entities.User;
import me.ablax.unigram.models.PhotoDto;
import me.ablax.unigram.models.UserDto;
import me.ablax.unigram.repositories.PhotoRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final UserService userService;

    public PhotoService(final PhotoRepository photoRepository, final UserService userService) {
        this.photoRepository = photoRepository;
        this.userService = userService;
    }

    public List<PhotoDto> getAllPhotos(final UserDto loggedUser) {
        final List<Photo> allPhotos = photoRepository.findAll();
        return markLikedPhotos(allPhotos, loggedUser);
    }

    public List<PhotoDto> getAllPhotosBy(final UserDto user, final UserDto loggedUser) {
        final User photosBy = userService.findById(user.getUserId());
        final List<Photo> allByOwner = photoRepository.findAllByOwner(photosBy);
        return markLikedPhotos(allByOwner, loggedUser);
    }

    private List<PhotoDto> markLikedPhotos(final List<Photo> photoEntities, final UserDto loggedUser) {
        final List<PhotoDto> photos = photoEntities.stream()
                .sorted(Comparator.comparing(Photo::getCreatedAt).reversed())
                .map(Photo::toDto)
                .collect(Collectors.toList());

        final List<Long> likedPhotos = userService.getLikedPhotosIds(loggedUser.getUserId());

        photos.parallelStream().forEach(photoDto -> {
            if (likedPhotos.contains(photoDto.getPhotoId())) {
                photoDto.setLiked(true);
            }
        });

        return photos;
    }

    public void savePhoto(final Photo photo, final UserDto userDto) {
        final User user = userService.findById(userDto.getUserId());
        photo.setOwner(user);
        photoRepository.save(photo);
    }
}
