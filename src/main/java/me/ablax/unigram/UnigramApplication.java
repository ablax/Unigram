package me.ablax.unigram;

import com.cloudinary.Cloudinary;
import com.cloudinary.SingletonManager;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class UnigramApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnigramApplication.class, args);
    }

    @PostConstruct
    public void setCloudinary() {
        final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "ablax-me",
                "api_key", "-0-0-0-0-0-0-0-0-0-0-0-",
                "api_secret", "0-0-0-0-0-0-0-0-0-0-0-0"));
        final SingletonManager manager = new SingletonManager();
        manager.setCloudinary(cloudinary);
        manager.init();
    }

}
