package edu.wgu.d387_sample_code.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.wgu.d387_sample_code.convertor.TimeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TimeController {

    private TimeServiceImpl timeService;

    @Autowired
    public TimeController(TimeServiceImpl timeService) {

        this.timeService = timeService;
    }

    @GetMapping("/timezone")
    public String timeZone() throws JsonProcessingException {

        this.timeService.addTimeZones();
        return this.timeService.timesToJson();
    }
}
