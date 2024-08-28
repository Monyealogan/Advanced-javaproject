package edu.wgu.d387_sample_code.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.wgu.d387_sample_code.convertor.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WelcomeController {

    private final WelcomeService welcomeService;

    @Autowired
    public WelcomeController(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }

    @GetMapping("/welcome")
    public String welcome() throws JsonProcessingException, InterruptedException {
        welcomeService.startService();
        return welcomeService.getJsonList();
    }
}
