package edu.wgu.d387_sample_code.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class WelcomeServiceImpl implements WelcomeService {

    private List<String> messages = new ArrayList<>();

    @Override
    public void startService() {
        CompletableFuture<Void> enUsFuture = CompletableFuture.runAsync(() -> loadProperties("welcome_en.properties"));
        CompletableFuture<Void> frCaFuture = CompletableFuture.runAsync(() -> loadProperties("welcome_fr.properties"));

        CompletableFuture<Void> allOf = CompletableFuture.allOf(enUsFuture, frCaFuture);
        try {
            allOf.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to load properties", e);
        }
    }

    private void loadProperties(String filename) {
        Properties properties = new Properties();
        try (InputStream stream = new ClassPathResource(filename).getInputStream()) {
            properties.load(stream);
            synchronized (this) {
                messages.add(properties.getProperty("welcome"));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + filename, e);
        }
    }

    @Override
    public String getJsonList() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this.messages);
    }
}
