package me.ablax.unigram.controllers;

/**
 * @author Murad Hamza on 23.2.2021 г.
 */

import com.cloudinary.Singleton;
import com.cloudinary.utils.ObjectUtils;
import me.ablax.unigram.entities.Photo;
import me.ablax.unigram.models.PhotoUpload;
import me.ablax.unigram.models.UserDto;
import me.ablax.unigram.services.PhotoService;
import me.ablax.unigram.services.UserService;
import me.ablax.unigram.utils.LoginUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/")
public class MainController {

    private static final String HOME_PAGE = "redirect:/";
    private final PhotoService photoService;
    private final UserService userService;

    public MainController(final PhotoService photoService, final UserService userService) {
        this.photoService = photoService;
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String homePage(final ModelMap model, final HttpSession session) {
        if (LoginUtils.isLoggedIn(session)) {
            model.addAttribute("photos", photoService.getAllPhotos(LoginUtils.getUser(session)));
            model.addAttribute("title", "Latest posts");
            return "images";
        } else {
            return "login";
        }
    }

    @GetMapping(value = "/register")
    public String register(final HttpSession httpSession) {
        if (LoginUtils.isLoggedIn(httpSession)) {
            return HOME_PAGE;
        }
        return "register";
    }

    @PostMapping(value = "/register")
    public String register(final HttpSession httpSession, final ModelMap model, @RequestParam final String username, @RequestParam final String email, @RequestParam final String password) {
        if (LoginUtils.isLoggedIn(httpSession)) {
            return HOME_PAGE;
        }
        final UserDto userDto;
        try {
            userDto = userService.registerUser(username, password, email);
        } catch (RuntimeException ex) {
            model.addAttribute("exception", ex.getMessage());
            return "register";
        }
        LoginUtils.login(httpSession, userDto);
        return HOME_PAGE;
    }

    @PostMapping("/login")
    public String loginAttempt(final ModelMap model, final HttpSession httpSession, @RequestParam final String username, @RequestParam final String password) {
        if (LoginUtils.isLoggedIn(httpSession)) {
            return HOME_PAGE;
        }
        final UserDto userDto;
        try {
            userDto = userService.loginUser(username, password);
        } catch (RuntimeException ex) {
            model.addAttribute("exception", ex.getMessage());
            return "login";
        }
        LoginUtils.login(httpSession, userDto);
        return HOME_PAGE;
    }

    @RequestMapping(value = "/logout")
    public String logout(final HttpSession session) {
        session.invalidate();
        return HOME_PAGE;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/upload")
    public String uploadPhoto(@ModelAttribute final PhotoUpload photoUpload, final String description, final HttpSession session) throws IOException {
        if (!LoginUtils.isLoggedIn(session)) {
            return HOME_PAGE;
        }
        if (photoUpload.getFile() != null && !photoUpload.getFile().isEmpty()) {
            final Map uploadResult;

            uploadResult = Singleton.getCloudinary().uploader().upload(photoUpload.getFile().getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            photoUpload.setPublicId((String) uploadResult.get("public_id"));
            final Object version = uploadResult.get("version");
            if (version instanceof Integer) {
                photoUpload.setVersion(new Long((Integer) version));
            } else {
                photoUpload.setVersion((Long) version);
            }

            photoUpload.setSignature((String) uploadResult.get("signature"));
            photoUpload.setFormat((String) uploadResult.get("format"));
            photoUpload.setResourceType((String) uploadResult.get("resource_type"));

            final Photo photo = new Photo();
            photo.setDescription(description);
            photo.setPublicId(photoUpload.getPublicId());
            photo.setFormat(photoUpload.getFormat());
            photoService.savePhoto(photo, LoginUtils.getUser(session));
        }
        return HOME_PAGE;
    }

    @GetMapping("/upload")
    public String uploadPhotoForm(final HttpSession session) {
        if (!LoginUtils.isLoggedIn(session)) {
            return HOME_PAGE;
        }
        return "post";
    }

    @PostMapping("/toggleLike")
    public @ResponseBody
    ResponseEntity<String> toggleLike(final HttpSession session, long picId) {
        if (!LoginUtils.isLoggedIn(session)) {
            return ResponseEntity.badRequest().build();
        }
        boolean toggled = userService.toggleLike(picId, LoginUtils.getUser(session));
        return ResponseEntity.status(toggled ? 200 : 201).build();
    }

}