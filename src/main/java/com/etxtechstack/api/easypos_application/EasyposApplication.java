package com.etxtechstack.api.easypos_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableConfigurationProperties
@EnableJpaAuditing
@EnableCaching
public class EasyposApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyposApplication.class, args);
    }

}
