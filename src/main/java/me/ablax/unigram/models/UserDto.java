package me.ablax.unigram.models;

/**
 * @author Murad Hamza on 23.2.2021 г.
 */
public class UserDto {
    private final Long userId;
    private final String username;

    public UserDto(final long userId, final String username) {
        this.userId = userId;
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

}
