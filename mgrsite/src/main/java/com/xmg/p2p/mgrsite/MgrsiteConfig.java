package com.xmg.p2p.mgrsite;

import com.xmg.p2p.CoreConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@Import({CoreConfig.class,WebMvcConfig.class})
@PropertySource("classpath:application.properties")
public class MgrsiteConfig {
    public static void main(String[] args) {

        SpringApplication.run(MgrsiteConfig.class,args);
    }
}
