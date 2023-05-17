package com.bnpl.catalogservice;

import com.bnpl.catalogservice.config.BnplProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final BnplProperties properties;

    public HomeController(BnplProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/")
    public String home() {
        return properties.getGreeting();
    }

}
