package me.ablax.unigram.entities;

import me.ablax.unigram.models.UserDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Murad Hamza on 23.2.2021 г.
 */
@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private String password;
    @OneToMany(mappedBy = "owner")
    private List<Photo> photos = new ArrayList<>();
    @ManyToMany
    private List<Photo> likedPhotos = new ArrayList<>();

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(final List<Photo> photos) {
        this.photos = photos;
    }

    public List<Photo> getLikedPhotos() {
        return likedPhotos;
    }

    public void setLikedPhotos(final List<Photo> likedPhotos) {
        this.likedPhotos = likedPhotos;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public UserDto toDto() {
        return new UserDto(id, username);
    }
}
