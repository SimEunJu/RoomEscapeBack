package com.sej.escape;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.sej.escape.repository")
public class EscapeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EscapeApplication.class, args);
    }

}
