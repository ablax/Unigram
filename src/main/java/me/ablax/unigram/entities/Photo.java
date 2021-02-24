package me.ablax.unigram.entities;

import me.ablax.unigram.models.PhotoDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import java.util.Date;

/**
 * @author Murad Hamza on 23.2.2021 г.
 */
@Entity(name = "photos")
public class Photo {
    private static final String CLOUDINARY_STORAGE = "https://res.cloudinary.com/ablax-me/image/upload/h_500,q_auto/";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(updatable = false, nullable = false)
    private String publicId;

    @Column(updatable = false, nullable = false)
    private String format;

    @Column(updatable = false, nullable = false)
    private Date createdAt;

    @ManyToOne
    private User owner;

    @PrePersist
    public void onCreate() {
        createdAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String title) {
        this.description = title;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(final String publicId) {
        this.publicId = publicId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(final String format) {
        this.format = format;
    }

    public String getImageUrl() {
        return CLOUDINARY_STORAGE + getPublicId() + "." + getFormat();
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(final User owner) {
        this.owner = owner;
    }

    public PhotoDto toDto() {
        return new PhotoDto(id, getImageUrl(), owner.toDto(), description);
    }
}