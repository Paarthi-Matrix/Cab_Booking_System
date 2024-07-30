package com.i2i.zapcab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.i2i.zapcab.service.SecurityAuditorAwareImpl;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableScheduling
public class ZapCab {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SecurityAuditorAwareImpl();
    }

    public static void main(String[] args) {
        SpringApplication.run(ZapCab.class, args);
    }
}
