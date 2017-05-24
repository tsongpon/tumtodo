package net.songpon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 */

@SpringBootApplication
@EnableAutoConfiguration
public class TumTodoApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(TumTodoApplication.class, args);
    }
}
