package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {


    private String greetingMessage;

    public WelcomeController(@Value("${welcome.message:DEFAULT}") String greetingMessage) {
        this.greetingMessage = greetingMessage;
    }

    @GetMapping("/")
    public String sayHello() {
        return this.greetingMessage;
    }

}
