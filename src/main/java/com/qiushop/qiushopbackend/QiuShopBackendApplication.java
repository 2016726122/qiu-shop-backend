package com.qiushop.qiushopbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class QiuShopBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(QiuShopBackendApplication.class, args);
    }

}
