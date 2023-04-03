package com.hippo.chatgpt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author xw
 */
@SpringBootApplication
@ComponentScan("com.hippo.chatgpt.*")
public class ChatGPTApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatGPTApplication.class, args);
    }
}
