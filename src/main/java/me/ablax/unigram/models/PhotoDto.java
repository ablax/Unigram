package me.ablax.unigram.models;

/**
 * @author Murad Hamza on 23.2.2021 г.
 */
public class PhotoDto {
    private final Long photoId;
    private final String url;
    private final UserDto owner;
    private final String description;
    private boolean liked;

    public PhotoDto(final Long photoId, final String url, final UserDto owner, final String description) {
        this.photoId = photoId;
        this.url = url;
        this.owner = owner;
        this.description = description;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(final boolean liked) {
        this.liked = liked;
    }

    public String getUrl() {
        return url;
    }

    public UserDto getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }
}
