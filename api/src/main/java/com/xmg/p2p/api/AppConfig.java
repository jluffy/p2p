package com.xmg.p2p.api;

import com.xmg.p2p.CoreConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({CoreConfig.class})
public class AppConfig {
    public static void main(String[] args) {

        SpringApplication.run(AppConfig.class,args);
    }

}
