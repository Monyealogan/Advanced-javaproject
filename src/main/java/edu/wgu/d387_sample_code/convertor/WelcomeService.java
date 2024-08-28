package edu.wgu.d387_sample_code.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface WelcomeService {
    void startService();
    String getJsonList() throws JsonProcessingException, InterruptedException;
}
