package com.spring.sharepod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SharepodApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharepodApplication.class, args);
    }

}
