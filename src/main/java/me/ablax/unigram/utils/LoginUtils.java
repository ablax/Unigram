package me.ablax.unigram.utils;

import me.ablax.unigram.models.UserDto;

import javax.servlet.http.HttpSession;

/**
 * @author Murad Hamza on 23.2.2021 г.
 */
public class LoginUtils {
    public static boolean isLoggedIn(final HttpSession session) {
        final Object userObj = session.getAttribute("user");
        if (userObj == null) {
            return false;
        }
        if (!(userObj instanceof UserDto)) {
            return false;
        }

        final UserDto user = (UserDto) userObj;
        return user.getUserId() != null;
    }

    public static void login(final HttpSession httpSession, final UserDto userDto) {
        httpSession.setAttribute("user", userDto);
    }

    public static UserDto getUser(final HttpSession session) {
        final Object userObj = session.getAttribute("user");
        if (userObj == null) {
            return null;
        }
        if (!(userObj instanceof UserDto)) {
            return null;
        }

        final UserDto user = (UserDto) userObj;
        return user;
    }
}
